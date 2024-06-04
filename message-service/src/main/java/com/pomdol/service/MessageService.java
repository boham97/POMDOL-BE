package com.pomdol.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomdol.dto.GroupReqDto;
import com.pomdol.dto.GroupResDto;
import com.pomdol.dto.MessageReqDto;
import com.pomdol.dto.MessageResDto;
import com.pomdol.entity.Message;
import com.pomdol.massagequeue.KafkaProducer;
import com.pomdol.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MessageService {
    private final ObjectMapper objectMapper;
    private final KafkaProducer kafkaProducer;
    private final MessageRepository messageRepository;
    @Transactional
    public void receiveAndSave(String jsonMessage){
        try {
            MessageReqDto messageReqDto = objectMapper.readValue(jsonMessage, MessageReqDto.class);
            log.info("message tracking uuid: {}", messageReqDto.getUuid());
            messageReqDto.setCreatedAt(LocalDateTime.now());
            messageRepository.save(messageReqDto.messageBuilder());
            kafkaProducer.messageSend("sub", messageReqDto);
        }catch (JsonProcessingException ex){
            log.error("objectMapper error message uuid: {}", jsonMessage);
            ex.printStackTrace();
        }
    }
    public void receive(String jsonMessage){
        try {
            MessageReqDto messageReqDto = objectMapper.readValue(jsonMessage, MessageReqDto.class);
            log.info("message tracking uuid: {}", messageReqDto.getUuid());
            kafkaProducer.messageSend("sub", messageReqDto);
        }catch (JsonProcessingException ex){
            log.error("objectMapper error message uuid: {}", jsonMessage);
            ex.printStackTrace();
        }
    }

    public List<MessageResDto> getBeforeMessage(int groupId, Pageable pageable) {
        return messageRepository.findByGroupId(groupId, pageable).getContent()
                .stream()
                .map(Message::messageDtoBuilder)
                .collect(Collectors.toList());
    }

    public ResponseEntity<GroupResDto> channelConnect(GroupReqDto groupReqDto) {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("createdAt", "ASC"));
        Page<Message> messagePage = messageRepository.findByGroupId(groupReqDto.getGroupId(),
                pageable);
        return ResponseEntity.ok()
                .body(new GroupResDto(
                        pageable.getPageNumber(),
                        messagePage.getTotalPages())
                );
    }
}

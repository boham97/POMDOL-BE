package com.pomdol.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomdol.dto.ChannelReqDto;
import com.pomdol.dto.ChannelResDto;
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

    public List<MessageResDto> getBeforeMessage(int groupId, int channelId, Pageable pageable) {
        return messageRepository.findByGroupIdAndChannelId(groupId, channelId, pageable).getContent()
                .stream()
                .map(Message::messageDtoBuilder)
                .collect(Collectors.toList());
    }

    public void channelConnect(String jsonMessage) {
        try {
            ChannelReqDto channelReqDto = objectMapper.readValue(jsonMessage, ChannelReqDto.class);
            log.info("message tracking uuid: {}", channelReqDto.getUuid());
            Pageable pageable = PageRequest.of(0, 20, Sort.by("createdAt", "ASC"));
            Page<Message> messagePage = messageRepository.findByGroupIdAndChannelId(channelReqDto.getGroupId(),
                    channelReqDto.getChannelId(),
                    pageable
            );
            pageable = PageRequest.of(messagePage.getTotalPages() - 1, 20, Sort.by("createdAt", "ASC"));
            List<MessageResDto> messageResDtoList = messageRepository.findByGroupIdAndChannelId(channelReqDto.getGroupId(),
                    channelReqDto.getChannelId(),
                    pageable).stream()
                    .map(Message::messageDtoBuilder)
                    .collect(Collectors.toList());
            kafkaProducer.simpleMessageSend("group", objectMapper.writeValueAsString(new ChannelResDto(messageResDtoList, messagePage.getTotalPages())));
        }catch (JsonProcessingException ex){
            log.error("objectMapper error message uuid: {}", jsonMessage);
            ex.printStackTrace();
        }
    }
}

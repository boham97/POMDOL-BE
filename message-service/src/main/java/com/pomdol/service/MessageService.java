package com.pomdol.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomdol.dto.MessageDto;
import com.pomdol.massagequeue.KafkaProducer;
import com.pomdol.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
@Slf4j
@RequiredArgsConstructor
@Service
public class MessageService {
    private final ObjectMapper objectMapper;
    private final KafkaProducer kafkaProducer;
    private final MessageRepository messageRepository;

    public void receiveAndSave(String jsonMessage){
        try {
            MessageDto messageDto = objectMapper.readValue(jsonMessage, MessageDto.class);
            log.info("message tracking uuid: {}", messageDto.getUuid());
            messageDto.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            messageRepository.save(messageDto.messageBuilder());
            kafkaProducer.messageSend("sub", messageDto);
        }catch (JsonProcessingException ex){
            log.error("objectMapper error message uuid: {}", jsonMessage);
            ex.printStackTrace();
        }
    }
    public void receive(String jsonMessage){
        try {
            MessageDto messageDto = objectMapper.readValue(jsonMessage, MessageDto.class);
            log.info("message tracking uuid: {}", messageDto.getUuid());
            messageDto.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            messageRepository.save(messageDto.messageBuilder());
            kafkaProducer.messageSend("sub", messageDto);
        }catch (JsonProcessingException ex){
            log.error("objectMapper error message uuid: {}", jsonMessage);
            ex.printStackTrace();
        }
    }
}

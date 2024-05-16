package com.pomdol.controller;

import com.pomdol.dto.ChatMessageDto;
import com.pomdol.massagequeue.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.UUID;

import java.time.LocalDateTime;
// 메세지를 카프카로 전달하기전 UUID를 넣어 메세지가 어는 레벨에서 문제가 생겼느지 파악
@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {
    private final Environment env;
    private final KafkaProducer kafkaProducer;
    private final SimpMessageSendingOperations messagingTemplate;
    @GetMapping("/health_check")
    public String status() {
        log.info("It's Working in Order Service on PORT {}", env.getProperty("local.server.port"));
        return String.format("It's Working in Order Service on PORT %s",
                env.getProperty("local.server.port"));
    }
    @MessageMapping("/group/{roomId}")
    public void receiveRoom(@DestinationVariable Long roomId, ChatMessageDto message) {
        message.setUuid(UUID.randomUUID());
        //카프카로 전송
        log.info("ChatController receiveRoom {} message: {}", roomId, message.getUserId());
        kafkaProducer.messageSend("pub", message);
    }

    public void sendRoom(Long groupId, ChatMessageDto message){
        log.info("ChatController sendRoom: {} message: {}", groupId, message.getUuid());
        messagingTemplate.convertAndSend(String.format("/sub/group/%s", groupId), message);
    }

    @MessageMapping("/user/{userId}")
    public void receiveUser(@DestinationVariable Long userId, ChatMessageDto message) {
        message.setUuid(UUID.randomUUID());
        log.info("ChatController receiveUser {} message: {}", userId, message.getUuid());
        kafkaProducer.messageSend("pub", message);
    }

    public void sendUser(Long userId, ChatMessageDto message){
        log.info("ChatController sendUser: {} message: {}", userId, message.getUuid());
        messagingTemplate.convertAndSend(String.format("/sub/user/%s", userId), message);
    }
}

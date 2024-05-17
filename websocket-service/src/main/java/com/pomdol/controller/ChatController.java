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
    @MessageMapping("/group/{groupId}")
    public void receiveRoom(@DestinationVariable Integer groupId, ChatMessageDto message) {
        message.setUuid(UUID.randomUUID());
        message.setGroupId(groupId);
        //카프카로 전송
        log.info("ChatController receiveRoom {} message: {}", groupId, message.getUserId());
        kafkaProducer.messageSend("pub", message);
    }

    public void sendRoom(Integer groupId, ChatMessageDto message){
        log.info("ChatController sendRoom: {} message: {}", groupId, message.getUuid());
        messagingTemplate.convertAndSend(String.format("/sub/group/%s", groupId), message);
    }

    public void sendUser(Integer userId, ChatMessageDto message){
        log.info("ChatController sendUser: {} message: {}", userId, message.getUuid());
        messagingTemplate.convertAndSend(String.format("/sub/user/%s", userId), message);
    }
}

package com.pomdol.controller;

import com.pomdol.dto.MessageDto;
import com.pomdol.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequestMapping("/message")
@RequiredArgsConstructor
@RestController
public class MessageController {
    private final Environment env;
    private final MessageRepository messageRepository;
    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in Order Service on PORT %s",
                env.getProperty("local.server.port"));
    }
    @PostMapping("/test")
    public ResponseEntity<Boolean> test(){
        MessageDto messageDto = MessageDto.builder()
                .groupId(1)
                .channelId(1)
                .type(1)
                .userId(1)
                .content("234")
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .uuid(UUID.randomUUID())
                .build();
        System.out.println(messageDto.toString());
        messageRepository.save(messageDto.messageBuilder());
        return ResponseEntity.status(201).body(true);
    }
}

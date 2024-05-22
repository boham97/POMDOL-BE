package com.pomdol.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomdol.dto.ChannelReqDto;
import com.pomdol.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Slf4j
@RequestMapping("/message")
@RequiredArgsConstructor
@RestController
public class MessageController {
    private final Environment env;
    private final MessageService messageService;
    private final ObjectMapper objectMapper;
    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in Order Service on PORT %s",
                env.getProperty("local.server.port"));
    }
    @GetMapping("/group/{groupId}/channel/{channelId}/before")
    public ResponseEntity<Object> getBeforeMessage(@PathVariable int groupId,
                                        @PathVariable int channelId,
                                        @PageableDefault(size = 20,
                                                sort = "createdAt",
                                                direction = Sort.Direction.ASC
                                        ) Pageable pageable){
        return ResponseEntity.ok().body(messageService.getBeforeMessage(groupId, channelId, pageable));
    }
}

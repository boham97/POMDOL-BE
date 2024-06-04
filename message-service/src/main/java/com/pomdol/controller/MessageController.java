package com.pomdol.controller;

import com.pomdol.dto.GroupReqDto;
import com.pomdol.dto.GroupResDto;
import com.pomdol.service.MessageService;
import io.micrometer.core.annotation.Timed;
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
    @Timed("api.status")
    @GetMapping("/health_check")
    public String status(){
        return String.format("It's Working in Message Service on PORT %s",
                env.getProperty("local.server.port"));
    }
    @Timed("api.message")
    @GetMapping("/group/{groupId}/before")
    public ResponseEntity<Object> getBeforeMessage(@PathVariable int groupId,
                                        @PageableDefault(size = 20,
                                                sort = "createdAt",
                                                direction = Sort.Direction.ASC
                                        ) Pageable pageable){
        return ResponseEntity.ok().body(messageService.getBeforeMessage(groupId, pageable));
    }
    @Timed("api.page")
    @GetMapping("/group/{groupId}/page")
    public ResponseEntity<GroupResDto> getBeforeMessage(@PathVariable int groupId){
        return messageService.channelConnect(new GroupReqDto(groupId, UUID.randomUUID()));
    }
}

package com.pomdol.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ChatMessageDto {
    private Integer groupId;
    private Integer channelId;
    private Integer type;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private UUID uuid;
}

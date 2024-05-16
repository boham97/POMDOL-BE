package com.pomdol.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ChatMessageDto {
    private long groupId;
    private int type;
    private long userId;
    private String content;
    private LocalDateTime localDateTime;
    private UUID uuid;
}

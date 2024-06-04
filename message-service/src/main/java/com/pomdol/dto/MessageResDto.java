package com.pomdol.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class MessageResDto {
    private Integer type;
    private Integer userId;
    private String content;
    private String createdAt;
    private UUID uuid;
}
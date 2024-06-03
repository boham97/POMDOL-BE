package com.pomdol.dto.kafka;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class KafkaMessageReqDto {
    private Integer groupId;
    private Integer channelId;
    private Integer type;
    private Integer userId;
    private String content;
    private LocalDateTime createdAt;
    private UUID uuid;
}

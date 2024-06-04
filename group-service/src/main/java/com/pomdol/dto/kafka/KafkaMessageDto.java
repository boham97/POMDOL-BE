package com.pomdol.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Data
public class KafkaMessageDto {
    private Integer groupId;
    private Integer userId;
    private Integer type;
    private UUID uuid;
}

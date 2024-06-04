package com.pomdol.dto;

import com.pomdol.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Data
public class KafkaMessageDto {
    private Integer groupId;
    private Integer userId;
    private Integer type;
    private UUID uuid;

    public Message messageBuilder(){
        return Message.builder()
                .groupId(this.groupId)
                .type(this.type)
                .userId(this.userId)
                .content("")
                .createdAt(LocalDateTime.now())
                .build();

    }
}

package com.pomdol.dto;

import com.pomdol.entity.Message;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;
@Builder
@Data
public class MessageDto {
    private Integer groupId;
    private Integer channelId;
    private Integer type;
    private Integer userId;
    private String content;
    private Timestamp createdAt;
    private UUID uuid;

    public Message messageBuilder(){
        return Message.builder()
                .groupId(this.getGroupId())
                .channelId(this.getChannelId())
                .type(this.getType())
                .userId(this.getUserId())
                .content(this.getContent())
                .createdAt(this.getCreatedAt())
                .build();
    }
}

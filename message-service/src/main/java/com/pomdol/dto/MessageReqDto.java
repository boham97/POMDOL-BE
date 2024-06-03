package com.pomdol.dto;

import com.pomdol.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageReqDto {
    private Integer groupId;
    private Integer channelId;
    private Integer type;
    private Integer userId;
    private String content;
    private LocalDateTime createdAt;
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

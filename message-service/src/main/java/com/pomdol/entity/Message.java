package com.pomdol.entity;

import com.pomdol.dto.MessageResDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Getter
@Document(collection = "message")
public class Message {
    @Id
    private String messageId;
    private Integer groupId;
    private Integer type;
    private Integer userId;
    private String content;
    private LocalDateTime createdAt;

    public MessageResDto messageDtoBuilder(){
        return MessageResDto.builder()
                .type(this.type)
                .userId(this.userId)
                .content(this.content)
                .createdAt(createdAt.format( DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
package com.pomdol.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;

@Builder
@Document(collection = "message")
public class Message {
    @Id
    private String messageId;
    private Integer groupId;
    private Integer channelId;
    private Integer type;
    private Integer userId;
    private String content;
    private Timestamp createdAt;

}
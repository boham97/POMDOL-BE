package com.pomdol.dto;

import com.pomdol.entity.Message;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@Data
public class MessageResDto {
    private Integer type;
    private Integer userId;
    private String content;
    private String createdAt;
    private UUID uuid;


    public List<MessageResDto> pageToList(Page<Message> messagePage) {
        return messagePage.getContent().stream()
                .map(Message::messageDtoBuilder)
                .collect(Collectors.toList());
    }
}
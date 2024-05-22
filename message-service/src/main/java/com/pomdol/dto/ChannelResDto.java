package com.pomdol.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Builder
@Data
public class ChannelResDto {
    private List<MessageResDto> messageResDtoList;
    private Integer maxSize;
    public ChannelResDto(List<MessageResDto> messageResDtoList, Integer maxSize){
        this.messageResDtoList = messageResDtoList;
        this.maxSize = maxSize;
    }
}

package com.pomdol.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Builder
@Data
public class ChannelResDto {
    private Integer totalPage;
    private Integer maxSize;
    public ChannelResDto(Integer totalPage, Integer maxSize){
        this.totalPage = totalPage;
        this.maxSize = maxSize;
    }
}

package com.pomdol.dto.kafka;

import lombok.Data;

import java.util.UUID;

@Data
public class KafkaChannelResDto {
    private Integer totalPage;
    private Integer maxSize;
    private UUID uuid;
    public KafkaChannelResDto(Integer totalPage, Integer maxSize){
        this.totalPage = totalPage;
        this.maxSize = maxSize;
    }
}

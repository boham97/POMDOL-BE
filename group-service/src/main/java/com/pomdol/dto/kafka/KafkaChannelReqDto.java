package com.pomdol.dto.kafka;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
public class KafkaChannelReqDto {
    private Integer groupId;
    private Integer channelId;
    private UUID uuid;
    public KafkaChannelReqDto(Integer groupId, Integer channelId, UUID uuid){
        this.groupId = groupId;
        this.channelId = channelId;
        this.uuid = uuid;
    }
}

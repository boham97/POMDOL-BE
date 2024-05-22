package com.pomdol.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
public class ChannelReqDto {
    private Integer groupId;
    private Integer channelId;
    private UUID uuid;
    public ChannelReqDto(Integer groupId, Integer channelId, UUID uuid){
        this.groupId = groupId;
        this.channelId = channelId;
        this.uuid = uuid;
    }
}

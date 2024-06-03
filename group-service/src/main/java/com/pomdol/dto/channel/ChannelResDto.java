package com.pomdol.dto.channel;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class ChannelResDto {
    private final Integer channelId;
    private final String name;
    private final Integer type;
}

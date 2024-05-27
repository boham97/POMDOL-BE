package com.pomdol.dto.channel;

import com.pomdol.domain.Channel;
import com.pomdol.domain.Group;
import lombok.Data;

@Data
public class ChannelCreateReqDto {
    private final Integer userId;
    private final String name;
    private final Integer type;
    public Channel toEntity(Group group){
        return Channel.builder()
                .name(this.name)
                .type(this.type)
                .group(group)
                .build();
    }
}

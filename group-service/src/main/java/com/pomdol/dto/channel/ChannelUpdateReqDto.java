package com.pomdol.dto.channel;

import com.pomdol.domain.Channel;
import com.pomdol.domain.Group;
import lombok.Data;
@Data
public class ChannelUpdateReqDto {
    private final Integer userId;
    private final String name;

    public Channel toEntity(Group group) {
        return Channel.builder()
                .name(this.name)
                .group(group)
                .build();
    }
}

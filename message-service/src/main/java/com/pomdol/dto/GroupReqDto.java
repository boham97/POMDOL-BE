package com.pomdol.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
public class GroupReqDto {
    private Integer groupId;
    private UUID uuid;
    public GroupReqDto(Integer groupId, UUID uuid){
        this.groupId = groupId;
        this.uuid = uuid;
    }
}

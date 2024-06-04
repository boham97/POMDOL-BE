package com.pomdol.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GroupResDto {
    private Integer totalPage;
    private Integer maxSize;
    public GroupResDto(Integer totalPage, Integer maxSize){
        this.totalPage = totalPage;
        this.maxSize = maxSize;
    }
}

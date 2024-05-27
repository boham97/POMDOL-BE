package com.pomdol.dto;

import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;
@Data
public class CategoryResDto {
    private final Integer id;
    private final String name;
}

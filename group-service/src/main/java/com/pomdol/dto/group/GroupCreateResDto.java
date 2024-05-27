package com.pomdol.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
@AllArgsConstructor
public class GroupCreateResDto {
    private Integer groupId;
    private String name;
    private Integer maxSize;
    private Boolean isPublic;
    private String profile;
    private Integer category;
    private String content;
}

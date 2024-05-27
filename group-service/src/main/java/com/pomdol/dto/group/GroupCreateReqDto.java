package com.pomdol.dto.group;

import com.pomdol.domain.Group;
import com.pomdol.domain.GroupCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
@Slf4j
@Data
public class GroupCreateReqDto {
    private Integer userId;
    private String name;
    private Integer maxSize;
    private Boolean isPublic;
    private String password;
    private MultipartFile profile;
    private Integer category;
    private String content;


    public Group dtoToEntity(GroupCategory groupCategory){
        return Group.builder()
                .name(this.getName())
                .isPublic(this.getIsPublic())
                .profile(this.profile.getContentType() == null ?
                        "https://avatars.githubusercontent.com/u/169625925?s=200&v=4"
                        :
                        "https://velog.velcdn.com/images/www_1216/post/9a190c41-3655-4feb-ba6c-da532ae88a98/ba9a7cbd.png")
                .content(this.getContent())
                .createdAt(LocalDateTime.now())
                .leaderId(this.getUserId())
                .maxSize(this.getMaxSize())
                .groupCategory(groupCategory)
                .password(this.password)
                .build();
    }
}

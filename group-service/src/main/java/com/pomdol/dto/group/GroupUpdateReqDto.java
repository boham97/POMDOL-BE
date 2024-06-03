package com.pomdol.dto.group;

import com.pomdol.domain.Group;
import com.pomdol.domain.GroupCategory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Data
public class GroupUpdateReqDto {
    private Integer userId;
    private String name;
    private Integer maxSize;
    private Boolean isPublic;
    private String password;
    private MultipartFile profile;
    private Integer category;
    private String content;


    public Group dtoToEntity(GroupCategory groupCategory, Group group){
        group.setName(this.name);
        group.setIsPublic(this.isPublic);
        group.setPassword(this.password);
        group.setProfile(this.profile.getContentType() == null ?
                "https://avatars.githubusercontent.com/u/169625925?s=200&v=4"
                :
                "https://velog.velcdn.com/images/www_1216/post/9a190c41-3655-4feb-ba6c-da532ae88a98/ba9a7cbd.png");
        group.setGroupCategory(groupCategory);
        group.setContent(this.getContent());
        return group;
    }
}

package com.pomdol.domain;

import com.pomdol.dto.group.GroupCreateResDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "group_table")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Integer id;
    @Column(nullable = false,
            length = 30, name="group_name")
    private String name;
    private Boolean isPublic;
    @Column(length = 30)
    private String password;
    private String profile;
    private String content;
    private LocalDateTime createdAt;
    private Integer leaderId;
    private Integer maxSize;
    @ColumnDefault("1")
    private Integer size;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_category_id")
    private GroupCategory groupCategory;

    @OneToMany(mappedBy = "group",
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<GroupUser> groupUserList;
    @OneToMany(mappedBy = "group",
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Channel> channelList;

    public GroupCreateResDto entityToDto(){
        return new GroupCreateResDto(
                this.id,
                this.name,
                this.maxSize,
                this.isPublic,
                this.profile,
                this.groupCategory.getId(),
                this.content
        );
    }
}

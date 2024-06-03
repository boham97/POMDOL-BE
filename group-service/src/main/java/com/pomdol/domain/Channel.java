package com.pomdol.domain;

import com.pomdol.dto.channel.ChannelResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private Integer id;
    @Column(nullable = false,
            length = 30, name="channel_name")
    private String name;
    @Column(name = "channel_type")
    private Integer type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    public ChannelResDto toDto(){
        return ChannelResDto.builder()
                .channelId(this.id)
                .name(this.name)
                .type(this.type)
                .build();
    }

    public void  updateName(String name){
        this.name = name;
    }
}

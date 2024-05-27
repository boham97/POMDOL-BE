package com.pomdol.service;

import com.pomdol.domain.Channel;
import com.pomdol.dto.*;
import com.pomdol.dto.channel.ChannelCreateReqDto;
import com.pomdol.dto.channel.ChannelResDto;
import com.pomdol.dto.channel.ChannelUpdateReqDto;
import com.pomdol.dto.group.GroupCreateReqDto;
import com.pomdol.dto.group.GroupCreateResDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GroupService {
    ResponseEntity<GroupCreateResDto> createGroup(GroupCreateReqDto groupCreateReqDto);
    ResponseEntity<List<CategoryResDto>> getCategory();
    ResponseEntity<ChannelResDto> createChannel(ChannelCreateReqDto channelCreateReqDto, Integer groupId);
    ResponseEntity<String> deleteGroup(Integer groupId, Integer userId);
    ResponseEntity<String> deleteChannel(Integer groupId, Integer channelId, Integer userId);
    ResponseEntity<String> inviteUser(Integer groupId, Integer targetId, Integer userId);
    ResponseEntity<String> updateChannel(ChannelUpdateReqDto channelUpdateReqDto, Integer groupId, Integer channelId);
    ResponseEntity<GroupCreateResDto> updateGroup(GroupCreateReqDto groupCreateReqDto, Integer userId);
    ResponseEntity updateLeader(Integer targetId, Integer userId);
    ResponseEntity deleteGroupUser(Integer GroupId, Integer targetId, Integer userId);

}

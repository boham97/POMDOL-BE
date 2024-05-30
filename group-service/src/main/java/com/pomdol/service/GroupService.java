package com.pomdol.service;

import com.pomdol.dto.*;
import com.pomdol.dto.channel.ChannelCreateReqDto;
import com.pomdol.dto.channel.ChannelResDto;
import com.pomdol.dto.channel.ChannelUpdateReqDto;
import com.pomdol.dto.group.GroupCreateReqDto;
import com.pomdol.dto.group.GroupCreateResDto;
import com.pomdol.dto.group.GroupUpdateReqDto;
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
    ResponseEntity<GroupCreateResDto> updateGroup(GroupUpdateReqDto groupUpdateReqDto, Integer groupId);
    ResponseEntity<String> updateLeader(Integer groupId, Integer targetId, Integer userId);
    ResponseEntity<String> deleteGroupUser(Integer groupId, Integer targetId, Integer userId);
    ResponseEntity<String> exitGroup(Integer groupId, Integer userId);
    ResponseEntity<List<GroupCreateResDto>> getGroupList(Integer userId);
    ResponseEntity<List<ChannelResDto>> getChannelList(Integer groupId, Integer userId);
}

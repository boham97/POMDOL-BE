package com.pomdol.controller;

import com.pomdol.dto.*;
import com.pomdol.dto.channel.ChannelCreateReqDto;
import com.pomdol.dto.channel.ChannelResDto;
import com.pomdol.dto.channel.ChannelUpdateReqDto;
import com.pomdol.dto.group.GroupCreateReqDto;
import com.pomdol.dto.group.GroupCreateResDto;
import com.pomdol.dto.group.GroupUpdateReqDto;
import com.pomdol.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class GroupController {
    private final Environment env;
    private final GroupService groupService;
    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in Order Service on PORT %s",
                env.getProperty("local.server.port"));
    }
    @GetMapping("/category")
    public ResponseEntity<List<CategoryResDto>> getCategory() {return groupService.getCategory();}
    @PostMapping("/start")
    public ResponseEntity<GroupCreateResDto> createGroup(GroupCreateReqDto groupCreateReqDto) throws IOException {return groupService.createGroup(groupCreateReqDto);}
    @PostMapping("/{groupId}/channel/start")
    public ResponseEntity<ChannelResDto> createChannel(@PathVariable Integer groupId, ChannelCreateReqDto channelCreateReqDto) {return groupService.createChannel(channelCreateReqDto, groupId);}
    @PostMapping("/{groupId}/invite/{targetId}")
    public ResponseEntity<String>  inviteUser(@PathVariable Integer groupId, @PathVariable Integer targetId, Integer userId) {return groupService.inviteUser(groupId, targetId,userId);}
    @PutMapping("/{groupId}/channel/{channelId}/modify")
    public ResponseEntity<String> updateChannel(@PathVariable Integer groupId, @PathVariable Integer channelId, ChannelUpdateReqDto channelUpdateReqDto) {return groupService.updateChannel(channelUpdateReqDto, groupId, channelId);}
    @PostMapping("/{groupId}/")
    public ResponseEntity<GroupCreateResDto> updateGroup(GroupUpdateReqDto groupUpdateReqDto, @PathVariable Integer groupId) { return groupService.updateGroup(groupUpdateReqDto, groupId);}
    @PostMapping("/{groupId}/leader/{targetId}")
    public ResponseEntity<String> updateLeader(@PathVariable Integer targetId, @PathVariable Integer groupId, Integer userId) { return groupService.updateLeader(groupId, targetId, userId);}
    @DeleteMapping("/{groupId}/leader/{targetId}")
    public ResponseEntity<String> deleteGroupUser(@PathVariable Integer targetId, @PathVariable Integer groupId, Integer userId) { return groupService.deleteGroupUser(groupId, targetId, userId);}
    @DeleteMapping("/{groupId}/out")
    public ResponseEntity<String> exitGroup(@PathVariable  Integer groupId, Integer userId){return groupService.exitGroup(groupId, userId);}
    @GetMapping("/all")
    public ResponseEntity<List<GroupCreateResDto>> getGroupList(Integer userId){return groupService.getGroupList(userId);}
    @GetMapping("/{groupId}")
    public ResponseEntity<List<ChannelResDto>> getGroupList(Integer userId, @PathVariable Integer groupId){return groupService.getChannelList(groupId, userId);}
}

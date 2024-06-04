package com.pomdol.service;

import com.pomdol.dto.*;
import com.pomdol.dto.group.GroupCreateReqDto;
import com.pomdol.dto.group.GroupCreateResDto;
import com.pomdol.dto.group.GroupUpdateReqDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GroupService {
    ResponseEntity<GroupCreateResDto> createGroup(GroupCreateReqDto groupCreateReqDto);
    ResponseEntity<List<CategoryResDto>> getCategory();
    ResponseEntity<String> deleteGroup(Integer groupId, Integer userId);
    ResponseEntity<String> inviteUser(Integer groupId, Integer targetId, Integer userId);
    ResponseEntity<String> inviteUserRollBack(Integer groupId, Integer targetId);
    ResponseEntity<GroupCreateResDto> updateGroup(GroupUpdateReqDto groupUpdateReqDto, Integer groupId);
    ResponseEntity<String> updateLeader(Integer groupId, Integer targetId, Integer userId);
    ResponseEntity<String> deleteGroupUser(Integer groupId, Integer targetId, Integer userId);
    ResponseEntity<String> deleteGroupUserRollback(Integer groupId, Integer targetId);
    ResponseEntity<String> exitGroup(Integer groupId, Integer userId);
    ResponseEntity<String> exitGroupRollback(Integer groupId, Integer userId);
    ResponseEntity<List<GroupCreateResDto>> getGroupList(Integer userId);
}

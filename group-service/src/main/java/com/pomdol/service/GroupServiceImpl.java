package com.pomdol.service;

import com.pomdol.domain.Channel;
import com.pomdol.domain.Group;
import com.pomdol.domain.GroupCategory;
import com.pomdol.domain.GroupUser;
import com.pomdol.dto.*;
import com.pomdol.dto.channel.ChannelCreateReqDto;
import com.pomdol.dto.channel.ChannelResDto;
import com.pomdol.dto.channel.ChannelUpdateReqDto;
import com.pomdol.dto.group.GroupCreateReqDto;
import com.pomdol.dto.group.GroupCreateResDto;
import com.pomdol.dto.group.GroupUpdateReqDto;
import com.pomdol.exception.CustomException;
import com.pomdol.exception.ErrorCode;
import com.pomdol.repository.ChannelRepository;
import com.pomdol.repository.GroupCategoryRepository;
import com.pomdol.repository.GroupRepository;
import com.pomdol.repository.GroupUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GroupServiceImpl implements GroupService{
    private final GroupRepository groupRepository;
    private final GroupCategoryRepository groupCategoryRepository;
    private final ChannelRepository channelRepository;
    private final GroupUserRepository groupUserRepository;
    @Transactional
    @Override
    public ResponseEntity<GroupCreateResDto> createGroup(GroupCreateReqDto groupCreateReqDto) {
        if (groupCreateReqDto.getName().length() > 30){throw new CustomException(ErrorCode.NAME_TOO_LONG);}
        if (!groupCreateReqDto.getIsPublic() && groupCreateReqDto.getPassword().isEmpty()){throw new CustomException(ErrorCode.PRIVATE_NEED_PASSWORD);}
        if (groupCreateReqDto.getProfile().getContentType() != null &&
                !(groupCreateReqDto.getProfile().getContentType().equals("image/jpeg") ||
                        groupCreateReqDto.getProfile().getContentType().equals("image/png"))){
            log.info("media type: {}", groupCreateReqDto.getProfile().getContentType());
            throw new CustomException(ErrorCode.NOT_SUPPORTED_MEDIA_TYPE);}
        if (groupCreateReqDto.getProfile().getSize() > 100000){ throw new CustomException(ErrorCode.IMAGE_TO_LARGE);}
        Group group = groupRepository.save(
                groupCreateReqDto.dtoToEntity(
                        groupCategoryRepository.findById(groupCreateReqDto.getCategory()).orElseThrow(
                                () ->new CustomException(ErrorCode.CATEGORY_NOT_FOUND
                        ))));
        groupUserRepository.save(GroupUser.builder()
                        .userId(groupCreateReqDto.getUserId())
                        .createdAt(LocalDateTime.now())
                        .group(group)
                .build());
        return ResponseEntity.status(201).body(group.entityToDto());
    }

    @Override
    public ResponseEntity<List<CategoryResDto>> getCategory() {
        return ResponseEntity
                .status(200)
                .body(StreamSupport.stream(groupCategoryRepository.findAll().spliterator(), false)
                        .map(GroupCategory::toDto)
                        .collect(Collectors.toList()));
    }
    @Transactional
    @Override
    public ResponseEntity<ChannelResDto> createChannel(ChannelCreateReqDto channelCreateReqDto, Integer groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() ->new CustomException(ErrorCode.GROUP_NOT_FOUND));
        if (!group.getLeaderId().equals(channelCreateReqDto.getUserId())){ throw new CustomException((ErrorCode.NOT_GROUP_LEADER));}
        if (channelCreateReqDto.getName().length() > 30){ throw new CustomException((ErrorCode.CHANNEL_TOO_LONG));}
        return ResponseEntity
                .status(201)
                .body(channelRepository.save(channelCreateReqDto.toEntity(group)).toDto());
    }
    @Transactional
    @Override
    public ResponseEntity<String> deleteGroup(Integer groupId, Integer userId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() ->new CustomException(ErrorCode.GROUP_NOT_FOUND));
        if (!group.getLeaderId().equals(userId)){ throw new CustomException((ErrorCode.NOT_GROUP_LEADER));}
        groupRepository.delete(group);
        return ResponseEntity
                .status(204)
                .body("삭제 성공");
    }
    @Transactional
    @Override
    public ResponseEntity<String> deleteChannel(Integer groupId, Integer channelId, Integer userId) {
        Channel channel = channelRepository.findByGroupIdAndChannelIdAndUserId(groupId, channelId, userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_GROUP_LEADER));
        channelRepository.delete(channel);
        return ResponseEntity
                .status(204)
                .body("삭제 성공");
    }
    @Transactional
    @Override
    public ResponseEntity<String> inviteUser(Integer groupId, Integer targetId, Integer userId) {
        Group group = groupRepository.findByIdFetchGroupUser(groupId).orElseThrow(() ->new CustomException(ErrorCode.GROUP_NOT_FOUND));
        if (!group.getLeaderId().equals(userId)){ throw new CustomException((ErrorCode.NOT_GROUP_LEADER));}
        if (group.getSize() >= group.getMaxSize()){ throw new CustomException((ErrorCode.GROUP_FULL_SIZE));}
        if (group.getGroupUserList().stream()
                        .anyMatch(groupUser -> groupUser.getUserId().equals(targetId) &&
                                !groupUser.getIsDeleted())){
            throw new CustomException(ErrorCode.GROUP_USER_ALREADY_JOINED);
        }
        group.setSize(group.getSize() + 1);
        groupRepository.save(group);
        groupUserRepository.save(GroupUser.builder()
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .group(group)
                .build());
        return ResponseEntity
                .status(204)
                .body("초대 성공");
    }
    @Transactional
    @Override
    public ResponseEntity<String> updateChannel(ChannelUpdateReqDto channelUpdateReqDto, Integer groupId, Integer channelId) {
        Group group = groupRepository.findByIdFetchChannelList(groupId).orElseThrow(() ->new CustomException(ErrorCode.GROUP_NOT_FOUND));
        if (!group.getLeaderId().equals(channelUpdateReqDto.getUserId())){ throw new CustomException((ErrorCode.NOT_GROUP_LEADER));}
        if (channelUpdateReqDto.getName().length() > 30){ throw new CustomException((ErrorCode.CHANNEL_TOO_LONG));}

        Channel channel = group.getChannelList().stream()
                .filter(channelEntity -> channelEntity.getId().equals(channelId))
                .findFirst()
                .orElseThrow(() -> {throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);});
        channel.updateName(channelUpdateReqDto.getName());
        channelRepository.save(channel);
        return ResponseEntity
                .status(204)
                .body("수정 성공");
    }
    @Transactional
    @Override
    public ResponseEntity<GroupCreateResDto> updateGroup(GroupUpdateReqDto groupUpdateReqDto, Integer groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() ->new CustomException(ErrorCode.GROUP_NOT_FOUND));
        if (!group.getLeaderId().equals(groupUpdateReqDto.getUserId())){ throw new CustomException((ErrorCode.NOT_GROUP_LEADER));}
        if (groupUpdateReqDto.getName().length() > 30){throw new CustomException(ErrorCode.NAME_TOO_LONG);}
        if (!groupUpdateReqDto.getIsPublic() && groupUpdateReqDto.getPassword().isEmpty()){throw new CustomException(ErrorCode.PRIVATE_NEED_PASSWORD);}
        if (groupUpdateReqDto.getProfile().getContentType() != null &&
                !(groupUpdateReqDto.getProfile().getContentType().equals("image/jpeg") ||
                        groupUpdateReqDto.getProfile().getContentType().equals("image/png"))){
            log.info("media type: {}", groupUpdateReqDto.getProfile().getContentType());
            throw new CustomException(ErrorCode.NOT_SUPPORTED_MEDIA_TYPE);}
        if (groupUpdateReqDto.getProfile().getSize() > 100000){ throw new CustomException(ErrorCode.IMAGE_TO_LARGE);}

        group = groupRepository.save(
                groupUpdateReqDto.dtoToEntity(
                        groupCategoryRepository.findById(groupUpdateReqDto.getCategory()).orElseThrow(
                                () ->new CustomException(ErrorCode.CATEGORY_NOT_FOUND
                                )), group));
        return ResponseEntity.status(201).body(group.entityToDto());
    }
    @Transactional
    @Override
    public ResponseEntity<String> updateLeader(Integer groupId, Integer targetId, Integer userId) {
        if (targetId.equals(userId)) throw new CustomException(ErrorCode.CANT_DO_IT_YOURSELF);
        Group group = groupRepository.findByIdFetchGroupUser(groupId).orElseThrow(() ->new CustomException(ErrorCode.GROUP_NOT_FOUND));
        if (!group.getLeaderId().equals(userId)){ throw new CustomException((ErrorCode.NOT_GROUP_LEADER));}
        group.getGroupUserList().stream()
                .filter(groupUser1 -> groupUser1.getUserId().equals(targetId) && !groupUser1.getIsDeleted())
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_USER_NOT_FOUND));
        group.setLeaderId(targetId);
        groupRepository.save(group);
        return ResponseEntity.status(201).body("강퇴 성공");
    }
    @Transactional
    @Override
    public ResponseEntity<String> deleteGroupUser(Integer groupId, Integer targetId, Integer userId) {
        if (targetId.equals(userId)) throw new CustomException(ErrorCode.CANT_DO_IT_YOURSELF);
        Group group = groupRepository.findByIdFetchGroupUser(groupId).orElseThrow(() ->new CustomException(ErrorCode.GROUP_NOT_FOUND));
        if (!group.getLeaderId().equals(userId)){ throw new CustomException((ErrorCode.NOT_GROUP_LEADER));}
        GroupUser groupUser = group.getGroupUserList().stream()
                .filter(groupUser1 -> groupUser1.getUserId().equals(targetId) && !groupUser1.getIsDeleted())
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_USER_NOT_FOUND));
        groupUser.setIsDeleted(true);
        groupUserRepository.save(groupUser);
        return ResponseEntity.status(201).body("강퇴 성공");
    }
}

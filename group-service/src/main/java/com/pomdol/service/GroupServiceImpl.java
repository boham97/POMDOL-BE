package com.pomdol.service;

import com.pomdol.domain.Group;
import com.pomdol.domain.GroupCategory;
import com.pomdol.domain.GroupUser;
import com.pomdol.dto.*;
import com.pomdol.dto.group.GroupCreateReqDto;
import com.pomdol.dto.group.GroupCreateResDto;
import com.pomdol.dto.group.GroupUpdateReqDto;
import com.pomdol.dto.kafka.KafkaMessageDto;
import com.pomdol.exception.CustomException;
import com.pomdol.exception.ErrorCode;
import com.pomdol.massagequeue.KafkaProducer;
import com.pomdol.repository.GroupCategoryRepository;
import com.pomdol.repository.GroupRepository;
import com.pomdol.repository.GroupUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class GroupServiceImpl implements GroupService{
    private final GroupRepository groupRepository;
    private final GroupCategoryRepository groupCategoryRepository;
    private final GroupUserRepository groupUserRepository;
    private final KafkaProducer kafkaProducer;
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
    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<List<CategoryResDto>> getCategory() {
        return ResponseEntity
                .status(200)
                .body(StreamSupport.stream(groupCategoryRepository.findAll().spliterator(), false)
                        .map(GroupCategory::toDto)
                        .collect(Collectors.toList()));
    }
    @Override
    public ResponseEntity<String> deleteGroup(Integer groupId, Integer userId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() ->new CustomException(ErrorCode.GROUP_NOT_FOUND));
        if (!group.getLeaderId().equals(userId)){ throw new CustomException((ErrorCode.NOT_GROUP_LEADER));}
        groupRepository.delete(group);
        return ResponseEntity
                .status(204)
                .body("삭제 성공");
    }
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
                .userId(targetId)
                .createdAt(LocalDateTime.now())
                .group(group)
                .build());

        kafkaProducer.sendQuitMessage("group", new KafkaMessageDto(groupId, targetId, 3, UUID.randomUUID()));

        return ResponseEntity
                .status(204)
                .body("초대 성공");
    }

    @Override
    public ResponseEntity<String> inviteUserRollBack(Integer groupId, Integer targetId) {
        Group group = groupRepository.findByIdFetchGroupUser(groupId).orElseThrow(() ->new CustomException(ErrorCode.GROUP_NOT_FOUND));
        if (group.getSize() >= group.getMaxSize()){ throw new CustomException((ErrorCode.GROUP_FULL_SIZE));}
        GroupUser groupUser = group.getGroupUserList().stream()
                .filter(groupUser1 -> groupUser1.getUserId().equals(targetId) &&
                        !groupUser1.getIsDeleted())
                        .findFirst()
                                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_USER_NOT_FOUND));


        group.setSize(group.getSize() - 1);
        groupRepository.save(group);
        groupUserRepository.delete(groupUser);

        return ResponseEntity
                .status(204)
                .body("초대  롤백 성공");
    }

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
        return ResponseEntity.status(201).body("강퇴 성공");
    }
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
        group.setSize(group.getSize() - 1);
        groupRepository.save(group);
        kafkaProducer.sendQuitMessage("group", new KafkaMessageDto(groupId, targetId, 4, UUID.randomUUID()));
        return ResponseEntity.status(201).body("강퇴 성공");
    }

    @Override
    public ResponseEntity<String> deleteGroupUserRollback(Integer groupId, Integer targetId) {
        Group group = groupRepository.findByIdFetchGroupUser(groupId).orElseThrow(() ->new CustomException(ErrorCode.GROUP_NOT_FOUND));
        GroupUser groupUser = group.getGroupUserList().stream()
                .filter(groupUser1 -> groupUser1.getUserId().equals(targetId) && groupUser1.getIsDeleted())
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_USER_NOT_FOUND));
        groupUser.setIsDeleted(false);
        groupUserRepository.save(groupUser);
        group.setSize(group.getSize() + 1);
        groupRepository.save(group);
        return ResponseEntity.status(201).body("강퇴 롤백 성공");
    }

    @Override
    public ResponseEntity<String> exitGroup(Integer groupId, Integer userId) {
        Group group = groupRepository.findByIdFetchGroupUser(groupId).orElseThrow(() ->new CustomException(ErrorCode.GROUP_NOT_FOUND));
        Optional<GroupUser> nextLeader = group.getGroupUserList().stream()
                .filter(groupUser -> groupUser.getUserId().equals(userId) && !groupUser.getIsDeleted())
                .min(Comparator.comparing(GroupUser::getCreatedAt));
        nextLeader.ifPresent(groupUser -> group.setLeaderId(groupUser.getUserId()));
        GroupUser groupUser = group.getGroupUserList().stream()
                .filter(groupUser1 -> groupUser1.getUserId().equals(userId) && !groupUser1.getIsDeleted())
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_USER_NOT_FOUND));
        groupUser.setIsDeleted(true);
        groupUserRepository.save(groupUser);
        group.setSize(group.getSize() - 1);
        groupRepository.save(group);
        kafkaProducer.sendQuitMessage("group", new KafkaMessageDto(groupId, userId, 3, UUID.randomUUID()));
        return ResponseEntity.status(201).body("그룹 나가기 성공");
    }

    @Override
    public ResponseEntity<String> exitGroupRollback(Integer groupId, Integer userId) {
        Group group = groupRepository.findByIdFetchGroupUser(groupId).orElseThrow(() ->new CustomException(ErrorCode.GROUP_NOT_FOUND));

        GroupUser groupUser = group.getGroupUserList().stream()
                .filter(groupUser1 -> groupUser1.getUserId().equals(userId) && groupUser1.getIsDeleted())
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_USER_NOT_FOUND));
        groupUser.setIsDeleted(false);
        groupUserRepository.save(groupUser);
        group.setSize(group.getSize() + 1);
        group.setLeaderId(userId);
        groupRepository.save(group);
        kafkaProducer.sendQuitMessage("group", new KafkaMessageDto(groupId, userId, 3, UUID.randomUUID()));
        return ResponseEntity.status(201).body("그룹 나가기 성공");
    }

    @Override
    public ResponseEntity<List<GroupCreateResDto>> getGroupList(Integer userId) {
        return ResponseEntity.status(200)
                .body(groupUserRepository.findByUserIdFetchGroup(userId)
                        .stream()
                        .map(groupUser -> groupUser.getGroup().entityToDto())
                        .collect(Collectors.toList()));
    }
}

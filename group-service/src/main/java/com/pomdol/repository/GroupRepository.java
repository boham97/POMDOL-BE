package com.pomdol.repository;

import com.pomdol.domain.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GroupRepository extends CrudRepository<Group, Integer> {
    @Query("SELECT g FROM Group g JOIN FETCH g.groupUserList WHERE g.id = :groupId")
    Optional<Group> findByIdFetchGroupUser(Integer groupId);
    @Query("SELECT g FROM Group g JOIN FETCH g.channelList WHERE g.id = :groupId")
    Optional<Group> findByIdFetchChannelList(Integer groupId);
}

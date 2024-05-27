package com.pomdol.repository;

import com.pomdol.domain.Channel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChannelRepository extends CrudRepository<Channel, Integer> {
    @Query("select c from Channel c join fetch c.group g where g.leaderId = :userId and g.id = :groupId and c.id = :channelId")
    Optional<Channel> findByGroupIdAndChannelIdAndUserId(Integer groupId, Integer channelId, Integer userId);
}

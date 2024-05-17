package com.pomdol.repository;

import com.pomdol.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByGroupIdOrderByMessageId(Integer groupId);
}

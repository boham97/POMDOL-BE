package com.pomdol.massagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomdol.dto.kafka.KafkaMessageDto;
import com.pomdol.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final GroupService groupService;
    private final ObjectMapper objectMapper;
    @KafkaListener(topics = "group_rollback")
    public void receive(String jsonMessage) throws IOException {
        try {
            KafkaMessageDto messageDto = objectMapper.readValue(jsonMessage, KafkaMessageDto.class);
            log.info("message tracking uuid: {}", messageDto.getUuid());
            Integer groupId = messageDto.getGroupId();
            Integer userId = messageDto.getUserId();
            Integer type = messageDto.getType();
            if (type.equals(3)){
                groupService.inviteUserRollBack(groupId, userId);
            }else if(type.equals(4)){
                groupService.exitGroupRollback(groupId, userId);
            }else{
                groupService.deleteGroupUserRollback(groupId, userId);
            }
        }catch (JsonProcessingException ex){
            log.error("objectMapper error message uuid: {}", jsonMessage);
            ex.printStackTrace();
        }
    }


}

package com.pomdol.massagequeue;

import com.pomdol.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final MessageService messageService;

    //웹소켓 서버로 받은 메세지 TimeStamp 생성후 저장
    // 웹소켓 서버로 다시 뿌리기
    @KafkaListener(topics = "pub")
    public void receive(String jsonMessage){
        messageService.receiveAndSave(jsonMessage);
    }
    //그룹변화 전파
    @KafkaListener(topics = "group")
    public void groupEvent(String jsonMessage){
        messageService.receive(jsonMessage);
    }
    @KafkaListener(topics = "leaveGroup")
    public void leaveGroupEvent(String jsonMessage){
        messageService.receiveAndSave(jsonMessage);
    }
    //유저 프로필 변화 채팅으로 전파
    //메세지는 저장X
    @KafkaListener(topics = "profile")
    public void profileEvent(String jsonMessage){
        messageService.receive(jsonMessage);
    }
}

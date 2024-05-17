package com.pomdol.massagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomdol.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void messageSend(String topic, MessageDto message){
        String jsonInString = "";
        try {
            jsonInString = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        kafkaTemplate.send(topic, jsonInString);
    }
}

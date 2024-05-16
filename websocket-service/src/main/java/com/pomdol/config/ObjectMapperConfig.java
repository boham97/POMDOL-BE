package com.pomdol.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// 자바 11은 기본적으로 JSR-310 스팩에 따라서 새로운 날짜와 시간 API 를 도입했습니다. 이 API 는 java.time 패키지에 포함되어 있습니다.
// 그러나 이전 버전의 Jackson 라이브러리는 이러한 새로운 날짜와 시간 API 를 지원하지 않았기 때문에,
// 자바 11에서 사용되는 LocalDateTime, ZonedDateTime 등의 날짜와 시간 객체를 Jackson 이 직렬화할 수 없었습니다.
// 이 문제를 해결하기 위해 Jackson 라이브러리에는 새로운 모듈인 JavaTimeModule 이 추가되었습니다.

@Configuration
public class ObjectMapperConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}

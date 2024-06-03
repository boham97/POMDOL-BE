package com.pomdol.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //== 200 ==//
    SUCCESS(HttpStatus.OK, "OK"),


    NOT_SUPPORTED_HTTP_METHOD(HttpStatus.BAD_REQUEST,"지원하지 않는 Http Method 방식입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 사용자를 찾을 수 없습니다."),
    ITEM_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 상품을 찾을 수 없습니다."),
    CANT_DO_IT_YOURSELF(HttpStatus.BAD_REQUEST, "자신입니다."),
    NAME_TOO_LONG(HttpStatus.BAD_REQUEST, "이름이 너무 깁니다."),
    NOT_SUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 미디어 타입입니다."),
    PRIVATE_NEED_PASSWORD(HttpStatus.BAD_REQUEST, "비공개시에만 비밀번호가 필요합니다"),
    IMAGE_TO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "이미지 크기가 너무 큽니다"),
    GROUP_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 그룹을 찾을 수 없습니다."),
    NOT_GROUP_LEADER(HttpStatus.BAD_REQUEST, "그룹장이 아닙니다."),
    GROUP_FULL_SIZE(HttpStatus.BAD_REQUEST, "정원을 초가했습니다."),
    CHANNEL_TOO_LONG(HttpStatus.BAD_REQUEST, "채널의 이름이 너무 깁니다."),
    CHANNEL_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 채널을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 카테고리를 찾을 수 없습니다."),
    GROUP_USER_ALREADY_JOINED(HttpStatus.BAD_REQUEST, "이미 가입한 유저입니다."),
    GROUP_USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 그룹 유저를 찾을 수 없습니다."),;
    private final HttpStatus status;
    private final String message;

}

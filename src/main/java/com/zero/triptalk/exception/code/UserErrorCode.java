package com.zero.triptalk.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "일치하는 회원이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String errorMessage;
}

package com.zero.triptalk.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PlaceErrorCode {

    ALREADY_ENROLL_PLACE(HttpStatus.NOT_FOUND, "이미 등록된 여행지입니다.");

    private final HttpStatus status;
    private final String errorMessage;
}

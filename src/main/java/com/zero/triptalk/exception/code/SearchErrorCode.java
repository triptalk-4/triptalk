package com.zero.triptalk.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SearchErrorCode {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "정렬 기준이 올바르지 않습니다."),
    RESULT_NOT_FOUND(HttpStatus.NOT_FOUND, "검색결과를 찾지 못했습니다.");

    private final HttpStatus status;
    private final String errorMessage;
}

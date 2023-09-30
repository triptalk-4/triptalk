package com.zero.triptalk.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PlannerErrorCode {


    NOT_FOUND_PLANNER(HttpStatus.BAD_REQUEST, "일정이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String errorMessage;
}

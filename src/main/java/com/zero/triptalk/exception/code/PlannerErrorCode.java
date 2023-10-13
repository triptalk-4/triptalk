package com.zero.triptalk.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PlannerErrorCode {


    NOT_FOUND_PLANNER(HttpStatus.BAD_REQUEST, "일정이 존재하지 않습니다."),
    UNMATCHED_USER_PLANNER(HttpStatus.BAD_REQUEST, "본인의 게시물만 수정/삭제할 수 있습니다."),
    UPDATE_PLANNER_FAILED(HttpStatus.BAD_REQUEST, "일정 업데이트 실패");

    private final HttpStatus status;
    private final String errorMessage;
}

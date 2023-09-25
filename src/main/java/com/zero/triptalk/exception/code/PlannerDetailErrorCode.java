package com.zero.triptalk.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PlannerDetailErrorCode {

    PLANNER_DETAIL_NOT_FOUNT(HttpStatus.NOT_FOUND, "일치하는 세부일정 정보가 존재하지 않습니다."),
    UNMATCHED_USER_PLANNER(HttpStatus.BAD_REQUEST, "본인의 게시물만 수정/삭제할 수 있습니다.");

    private final HttpStatus status;
    private final String errorMessage;
}
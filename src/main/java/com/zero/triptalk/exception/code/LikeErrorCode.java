package com.zero.triptalk.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LikeErrorCode {

    NO_Planner_Detail_Board(HttpStatus.NOT_FOUND, "상세일정 게시판이 없습니다.");


    private final HttpStatus status;
    private final String errorMessage;
}

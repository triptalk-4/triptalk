package com.zero.triptalk.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LikeErrorCode {

    NO_Planner_Detail_Board(HttpStatus.NOT_FOUND, "이미지 업로드 실패");


    private final HttpStatus status;
    private final String errorMessage;
}

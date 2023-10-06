package com.zero.triptalk.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LikeErrorCode {

    NO_Planner_Detail_Board(HttpStatus.NOT_FOUND, "게시물을 찾을수 없습니다."),
    NO_LIKE_DUPLICATE_ERROR(HttpStatus.BAD_REQUEST, "좋아요는 중복이 불가능 합니다.");


    private final HttpStatus status;
    private final String errorMessage;
}

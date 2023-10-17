package com.zero.triptalk.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LikeErrorCode {

    NO_Planner_Detail_Board(HttpStatus.NOT_FOUND, "게시물을 찾을수 없습니다."),
    NO_LIKE_DUPLICATE_ERROR(HttpStatus.BAD_REQUEST, "유저 저장은 중복이 불가능 합니다."),
    NO_SAVE_DUPLICATE_ERROR(HttpStatus.BAD_REQUEST, "저장함은 중복이 불가능 합니다."),
    NO_LIKE_SEARCH_ERROR(HttpStatus.BAD_REQUEST, "유저가 해당 게시글에 좋아요를 누르지 않았습니다"),
    No_User_Search(HttpStatus.BAD_REQUEST, "유저가 없습니다"),
    ALREADY_SAVE_USER(HttpStatus.BAD_REQUEST, "이미 유저가 좋아요를 저장하였습니다."),
    NO_SAVE_EXIST_ERROR(HttpStatus.BAD_REQUEST, "저장함의 데이터가 없습니다");
    private final HttpStatus status;
    private final String errorMessage;
}

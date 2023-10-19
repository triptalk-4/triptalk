package com.zero.triptalk.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReplyErrorCode {

    NO_PLANNER_DETAIL_BOARD(HttpStatus.NOT_FOUND, "일치하는 게시글이 존재하지 않습니다."),
    NO_PLANNER_DETAIL_REPLY_BOARD(HttpStatus.NOT_FOUND, "일치하는 댓글이 존재하지 않습니다."),
    NO_REPLY_OWNER(HttpStatus.NOT_FOUND, "댓글을 쓴 사람이 아닙니다." ),
    NO_Planner_Detail_Board(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다" );


    private final HttpStatus status;
    private final String errorMessage;
}

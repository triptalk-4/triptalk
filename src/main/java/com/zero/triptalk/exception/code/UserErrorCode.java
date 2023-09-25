package com.zero.triptalk.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "일치하는 회원이 존재하지 않습니다."),
    EMAIL_APPROVAL_DENIED(HttpStatus.BAD_REQUEST, "일치하는 회원이 존재하지 않습니다."),
    PASSWORD_APPROVAL_DENIED(HttpStatus.BAD_REQUEST, "비밀번호는 특수문자,영어,숫자를 포함해야 하며 , 8글자 이상이여야 합니다."),
    EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일 입니다."),
    NICKNAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임 입니다."),
    KAKAO_NICKNAME_ERROR(HttpStatus.BAD_REQUEST, "카카오 로그인을 다시 시도해 주세요!")
    ;


    private final HttpStatus status;
    private final String errorMessage;
}

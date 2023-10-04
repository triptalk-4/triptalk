package com.zero.triptalk.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ImageUploadErrorCode {

    IMAGE_UPLOAD_FAILED(HttpStatus.NOT_FOUND, "이미지 업로드 실패"),
    IMAGE_DELETE_FAILED(HttpStatus.NOT_FOUND, "이미지 삭제 실패"),
    BAD_REQUEST(HttpStatus.NOT_FOUND, "잘못된 이미지 형식입니다.");


    private final HttpStatus status;
    private final String errorMessage;
}

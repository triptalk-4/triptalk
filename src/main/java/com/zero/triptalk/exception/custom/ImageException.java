package com.zero.triptalk.exception.custom;

import com.zero.triptalk.exception.code.ImageUploadErrorCode;
import lombok.Getter;

@Getter
public class ImageException extends RuntimeException{

    private final ImageUploadErrorCode errorCode;
    public ImageException(ImageUploadErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}

package com.zero.triptalk.exception.type;

import com.zero.triptalk.exception.code.ImageUploadErrorCode;
import com.zero.triptalk.exception.code.PlaceErrorCode;
import lombok.Getter;

@Getter
public class ImageException extends RuntimeException{

    private final ImageUploadErrorCode errorCode;
    public ImageException(ImageUploadErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}

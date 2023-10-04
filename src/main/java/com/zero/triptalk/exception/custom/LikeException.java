package com.zero.triptalk.exception.custom;

import com.zero.triptalk.exception.code.LikeErrorCode;
import com.zero.triptalk.exception.code.UserErrorCode;
import lombok.Getter;

@Getter
public class LikeException extends RuntimeException{

    private final UserErrorCode errorCode;
    public LikeException(LikeErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}

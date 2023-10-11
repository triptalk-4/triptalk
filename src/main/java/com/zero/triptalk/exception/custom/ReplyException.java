package com.zero.triptalk.exception.custom;

import com.zero.triptalk.exception.code.ReplyErrorCode;
import lombok.Getter;

@Getter
public class ReplyException extends RuntimeException{

    private final ReplyErrorCode errorCode;
    public ReplyException(ReplyErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}

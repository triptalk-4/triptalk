package com.zero.triptalk.exception.custom;

import com.zero.triptalk.exception.code.AlertErrorCode;
import com.zero.triptalk.exception.code.LikeErrorCode;
import lombok.Getter;

@Getter
public class AlertException extends RuntimeException{

    private final AlertErrorCode errorCode;
    public AlertException(AlertErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}

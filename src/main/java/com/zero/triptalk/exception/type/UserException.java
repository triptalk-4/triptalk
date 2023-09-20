package com.zero.triptalk.exception.type;

import com.zero.triptalk.exception.code.PlannerDetailErrorCode;
import com.zero.triptalk.exception.code.UserErrorCode;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException{

    private final UserErrorCode errorCode;
    public UserException(UserErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}

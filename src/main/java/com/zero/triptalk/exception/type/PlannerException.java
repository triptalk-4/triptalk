package com.zero.triptalk.exception.type;

import com.zero.triptalk.exception.code.PlannerDetailErrorCode;
import com.zero.triptalk.exception.code.PlannerErrorCode;
import lombok.Getter;

@Getter
public class PlannerException extends RuntimeException{

    private final PlannerErrorCode errorCode;
    public PlannerException(PlannerErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }


}

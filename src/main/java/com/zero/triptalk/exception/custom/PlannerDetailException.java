package com.zero.triptalk.exception.custom;

import com.zero.triptalk.exception.code.PlannerDetailErrorCode;
import lombok.Getter;

@Getter
public class PlannerDetailException extends RuntimeException{

    private final PlannerDetailErrorCode errorCode;
    public PlannerDetailException(PlannerDetailErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}

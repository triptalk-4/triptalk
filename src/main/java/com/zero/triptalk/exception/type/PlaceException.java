package com.zero.triptalk.exception.type;

import com.zero.triptalk.exception.code.PlaceErrorCode;
import com.zero.triptalk.exception.code.UserErrorCode;
import lombok.Getter;

@Getter
public class PlaceException extends RuntimeException{

    private final PlaceErrorCode errorCode;
    public PlaceException(PlaceErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}

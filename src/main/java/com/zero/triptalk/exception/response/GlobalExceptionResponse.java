package com.zero.triptalk.exception.response;

import lombok.Getter;

@Getter
public class GlobalExceptionResponse {
    private final boolean result;
    private final String errorMessage;
    public GlobalExceptionResponse(String errorMessage) {
        this.result = false;
        this.errorMessage = errorMessage;
    }
}

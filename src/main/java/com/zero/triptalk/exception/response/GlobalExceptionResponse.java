package com.zero.triptalk.exception.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalExceptionResponse {
    private final HttpStatus status;
    private final String errorMessage;
    public GlobalExceptionResponse(HttpStatus status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }
}

package com.zero.triptalk.exception.handler;

import com.zero.triptalk.exception.response.GlobalExceptionResponse;
import com.zero.triptalk.exception.custom.PlannerDetailException;
import com.zero.triptalk.exception.custom.UserException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PlannerDetailException.class)
    protected GlobalExceptionResponse handlePlannerDetailException(PlannerDetailException e) {

        return new GlobalExceptionResponse(e.getErrorCode().getStatus(), e.getMessage());
    }

    @ExceptionHandler(UserException.class)
    protected GlobalExceptionResponse handleUserException(UserException e) {

        return new GlobalExceptionResponse(e.getErrorCode().getStatus(), e.getMessage());
    }

}

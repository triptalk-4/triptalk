package com.zero.triptalk.exception.handler;

import com.zero.triptalk.exception.response.GlobalExceptionResponse;
import com.zero.triptalk.exception.type.PlannerDetailException;
import com.zero.triptalk.exception.type.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PlannerDetailException.class)
    protected ResponseEntity<?> handlePlannerDetailException(PlannerDetailException e) {

        return ResponseEntity.status(e.getErrorCode().getStatus())
                                .body(new GlobalExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(UserException.class)
    protected ResponseEntity<?> handleUserException(UserException e) {

        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(new GlobalExceptionResponse(e.getMessage()));
    }

}

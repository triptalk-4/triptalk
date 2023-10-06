package com.zero.triptalk.exception.handler;

import com.zero.triptalk.exception.custom.LikeException;
import com.zero.triptalk.exception.response.GlobalExceptionResponse;
import com.zero.triptalk.exception.custom.PlannerDetailException;
import com.zero.triptalk.exception.custom.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PlannerDetailException.class)
    protected ResponseEntity<String> handlePlannerDetailException(PlannerDetailException e) {

        return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(UserException.class)
    protected ResponseEntity<String> handleUserException(UserException e) {

        return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(LikeException.class)
    protected ResponseEntity<String> handleLikeException(LikeException e) {

        return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getMessage());
    }

}

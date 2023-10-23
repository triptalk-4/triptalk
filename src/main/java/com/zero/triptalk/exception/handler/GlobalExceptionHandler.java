package com.zero.triptalk.exception.handler;

import com.zero.triptalk.exception.custom.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.NoSuchIndexException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
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

    @ExceptionHandler(ReplyException.class)
    protected ResponseEntity<String> handleReplyException(ReplyException e) {

        return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(SearchException.class)
    protected ResponseEntity<String> handleSearchException(SearchException e) {

        return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(PlannerException.class)
    protected ResponseEntity<String> handlePlannerException(PlannerException e) {

        return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(ImageException.class)
    protected ResponseEntity<String> handleImageException(ImageException e) {

        return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(PlaceException.class)
    protected ResponseEntity<String> handlePlaceException(PlaceException e) {

        return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(NoSuchIndexException.class)
    protected ResponseEntity<String> handlePlaceException(NoSuchIndexException e) {
        log.error(e.getIndex() + " -> " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(e.getIndex() + " -> " + e.getMessage());
    }
}

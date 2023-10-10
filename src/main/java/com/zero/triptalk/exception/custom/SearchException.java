package com.zero.triptalk.exception.custom;

import com.zero.triptalk.exception.code.SearchErrorCode;
import lombok.Getter;

@Getter
public class SearchException extends RuntimeException {
    private final SearchErrorCode errorCode;
    public SearchException(SearchErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}

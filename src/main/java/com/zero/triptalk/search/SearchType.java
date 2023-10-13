package com.zero.triptalk.search;

import com.zero.triptalk.exception.code.SearchErrorCode;
import com.zero.triptalk.exception.custom.SearchException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SearchType {
    DATE("date"),
    LIKE("likeCount"),
    VIEW("views");

    private final String field;

    public static String getSearchType(String searchType) {
        return Arrays.stream(SearchType.values()).filter(
                x -> x.name().equals(searchType)).findFirst().orElseThrow(() ->
                new SearchException(SearchErrorCode.INVALID_REQUEST)).getField();
    }
}

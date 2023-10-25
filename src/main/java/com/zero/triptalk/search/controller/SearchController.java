package com.zero.triptalk.search.controller;

import com.zero.triptalk.planner.dto.response.PlannerDetailSearchResponse;
import com.zero.triptalk.planner.dto.response.PlannerSearchResponse;
import com.zero.triptalk.search.service.SearchService;
import com.zero.triptalk.user.dto.UserInfoSearchResponse;
import com.zero.triptalk.user.dto.UserSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {

    private final SearchService searchService;
    @GetMapping("/main")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<PlannerSearchResponse>> getTop6Planners() {

        return ResponseEntity.ok(searchService.getTop6PlannersWithLikes());
    }

    @GetMapping("/search/{region}/{searchType}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<PlannerDetailSearchResponse>> searchByRegionAndSearchType(
                                                        @PathVariable String region,
                                                        @PathVariable String searchType,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "6") int size) {

        Pageable pageable = PageRequest.of(page, size);

     return ResponseEntity.ok(searchService.searchByRegionAnySort(region, searchType, pageable));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<UserSearchResponse>> getUserNicknameList(
                                                        @RequestParam String keyword,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "6") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(searchService.getUserNicknameList(keyword, pageable));
    }

    @GetMapping("/search/user/{userId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<UserInfoSearchResponse> searchByUserId(
                                                        @PathVariable Long userId,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "6") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(searchService.searchByUserId(userId, pageable));
    }

}

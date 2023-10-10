package com.zero.triptalk.like.controller;

import com.zero.triptalk.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/plans/detail/{plannerDetailId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> LikeOnePlus(@PathVariable Long plannerDetailId) {

            // LikeService에서 던진 예외를 캐치하고 처리합니다.
            Object response = likeService.createLikeOrPlusPlannerDetail(plannerDetailId);
            return ResponseEntity.ok(response);
    }
    @PostMapping("/minus/plans/detail/{plannerDetailId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> LikeOneMinus(@PathVariable Long plannerDetailId) {

        return ResponseEntity.ok(likeService.LikeOneMinus(plannerDetailId));
    }




}

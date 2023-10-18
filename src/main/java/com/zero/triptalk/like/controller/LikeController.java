package com.zero.triptalk.like.controller;

import com.zero.triptalk.like.dto.response.LikenOnePlusMinusResponse;
import com.zero.triptalk.like.dto.response.UserLikeAndSaveYnResponse;
import com.zero.triptalk.like.dto.response.UserSaveAndCancelResponse;
import com.zero.triptalk.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/plans/{plannerId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> LikeOnePlus(@PathVariable Long plannerId) {

            // LikeService에서 던진 예외를 캐치하고 처리합니다.
            LikenOnePlusMinusResponse response = likeService.createLikeOrPlusPlanner(plannerId);
            return ResponseEntity.ok(response);
    }
    @PostMapping("/minus/plans/{plannerId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<LikenOnePlusMinusResponse> LikeOneMinus(@PathVariable Long plannerId) {

        return ResponseEntity.ok(likeService.LikeOneMinus(plannerId));
    }

    @PostMapping("/plans/user/save/planner/{plannerId}")
    @PreAuthorize("hasAuthority('USER')")
    public UserSaveAndCancelResponse userSavePlus(@PathVariable Long plannerId) {

        return likeService.userSavePlus(plannerId);
    }

    @DeleteMapping("/plans/user/cancel/planner/{plannerId}")
    public UserSaveAndCancelResponse userSaveDelete(@PathVariable Long plannerId) {

        return likeService.userCancel(plannerId);
    }

    @GetMapping("/plans/user/check/save/like/{plannerId}")
    public UserLikeAndSaveYnResponse userCheckYn(@PathVariable Long plannerId) {

        return likeService.userCheckYn(plannerId);
    }



}

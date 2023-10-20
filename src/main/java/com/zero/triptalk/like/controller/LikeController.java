package com.zero.triptalk.like.controller;

import com.zero.triptalk.like.dto.response.LikenOnePlusMinusResponse;
import com.zero.triptalk.like.dto.response.UserLikeAndSaveYnResponse;
import com.zero.triptalk.like.dto.response.UserSaveAndCancelResponse;
import com.zero.triptalk.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/plans/{plannerId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> LikeOnePlus(@PathVariable Long plannerId, Principal principal) {

        LikenOnePlusMinusResponse response = likeService.createLikeOrPlusPlanner(plannerId, principal.getName());
        return ResponseEntity.ok(response);
    }
    @PostMapping("/minus/plans/{plannerId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<LikenOnePlusMinusResponse> LikeOneMinus(@PathVariable Long plannerId, Principal principal) {

        return ResponseEntity.ok(likeService.LikeOneMinus(plannerId, principal.getName()));
    }

    @PostMapping("/plans/user/save/planner/{plannerId}")
    @PreAuthorize("hasAuthority('USER')")
    public UserSaveAndCancelResponse userSavePlus(@PathVariable Long plannerId, Principal principal) {

        return likeService.userSavePlus(plannerId, principal.getName());
    }

    @DeleteMapping("/plans/user/cancel/planner/{plannerId}")
    @PreAuthorize("hasAuthority('USER')")
    public UserSaveAndCancelResponse userSaveDelete(@PathVariable Long plannerId, Principal principal) {

        return likeService.userCancel(plannerId, principal.getName());
    }

    @GetMapping("/plans/user/check/save/like/{plannerId}")
    @PreAuthorize("hasAuthority('USER')")
    public UserLikeAndSaveYnResponse userCheckYn(@PathVariable Long plannerId, Principal principal) {

        return likeService.userCheckYn(plannerId, principal.getName());
    }



}

package com.zero.triptalk.like.controller;

import com.zero.triptalk.like.dto.response.LikenOnePlusResponse;
import com.zero.triptalk.like.service.LikeService;
import com.zero.triptalk.planner.dto.PlannerDetailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/planner/plans/detail/{{plannerId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> LikeOnePlus(@PathVariable Long plannerId) {

        return ResponseEntity.ok(likeService.createPlannerDetail(plannerId));
    }


}

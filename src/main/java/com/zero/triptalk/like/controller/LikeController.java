package com.zero.triptalk.like.controller;

import com.zero.triptalk.exception.custom.LikeException;
import com.zero.triptalk.exception.custom.UserException;
import com.zero.triptalk.like.service.LikeService;
import com.zero.triptalk.planner.dto.PlannerDetailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/plans/detail/{plannerDetailId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> LikeOnePlus(@PathVariable Long plannerDetailId) {

        try {
            // LikeService에서 던진 예외를 캐치하고 처리합니다.
            Object response = likeService.createLikeOrPlusPlannerDetail(plannerDetailId);
            return ResponseEntity.ok(response);
        } catch (UserException e) {
            // 중복 "좋아요" 클릭에 대한 예외가 발생한 경우 에러 메시지를 클라이언트에게 반환합니다.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (LikeException e) {
            // 중복 "좋아요" 클릭에 대한 예외가 발생한 경우 에러 메시지를 클라이언트에게 반환합니다.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/minus/plans/detail/{plannerDetailId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> LikeOneMinus(@PathVariable Long plannerDetailId) {

        return ResponseEntity.ok(likeService.LikeOneMinus(plannerDetailId));
    }


}

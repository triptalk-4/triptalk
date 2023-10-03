package com.zero.triptalk.planner.controller;

import com.zero.triptalk.application.PlannerApplication;
import com.zero.triptalk.planner.dto.CreatePlannerInfo;
import com.zero.triptalk.planner.dto.PlannerDetailListResponse;
import com.zero.triptalk.planner.dto.PlannerDetailRequest;
import com.zero.triptalk.planner.dto.PlannerDetailResponse;
import com.zero.triptalk.planner.service.PlannerDetailService;
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
@RequestMapping("/api/plans")
public class PlannerController {

    private final PlannerDetailService plannerDetailService;
    private final PlannerApplication plannerApplication;

    //상세일정 한개 조회
    @GetMapping("/detail/{plannerDetailId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<PlannerDetailResponse> getPlannerDetail(@PathVariable Long plannerDetailId) {
        return ResponseEntity.ok(
                plannerDetailService.getPlannerDetail(plannerDetailId));
    }

    //세부일정 한개 생성
    @PostMapping("/detail/{plannerId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Boolean> createPlannerDetail(@PathVariable Long plannerId,
                                                       @RequestPart("files") List<MultipartFile> files,
                                                       @RequestPart PlannerDetailRequest request,
                                                       Principal principle) {

        return ResponseEntity.ok(plannerApplication.createPlannerDetail(plannerId, files, request, principle.getName()));
    }

    // 모든 상세일정 조회
    @GetMapping("/details")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getAllPlannerDetail() {
        List<PlannerDetailListResponse> list = plannerDetailService.getAllPlannerDetail();
        return ResponseEntity.ok(list);
    }

    //사진 리스트 저장
    @PostMapping("/{plannerId}/images")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<String>> uploadImages(@PathVariable Long plannerId,
                                                     @RequestPart("files") List<MultipartFile> files) {
        return ResponseEntity.ok(plannerDetailService.uploadImages(files));
    }

    //일정 생성
    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Boolean> createPlanner(@RequestBody CreatePlannerInfo info,
                                                 Principal principal) {

        return ResponseEntity.ok(plannerApplication.createPlanner(info.getPlannerRequest(), info.getPlannerDetailListRequests(), principal.getName()));
    }

    // 상세 일정 삭제
    @DeleteMapping("/details/{plannerDetailId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> deletePlannerDetail(@PathVariable Long plannerDetailId,
                                                    Principal principal) {
        plannerApplication.deletePlannerDetail(plannerDetailId, principal.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 일정 삭제
    @DeleteMapping("/{plannerId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> deletePlanner(@PathVariable Long plannerId,
                                              Principal principal) {
        plannerApplication.deletePlanner(plannerId, principal.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{plannerId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> updatePlannerDetail(@PathVariable Long plannerId,
                                                 @RequestPart List<MultipartFile> files,
                                                 @RequestPart PlannerDetailRequest request,
                                                 Principal principal) {

        return ResponseEntity.ok(plannerDetailService.updatePlannerDetail(files, request, principal.getName()));
    }

}
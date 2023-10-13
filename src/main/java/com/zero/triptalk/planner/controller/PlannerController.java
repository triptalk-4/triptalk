package com.zero.triptalk.planner.controller;

import com.zero.triptalk.application.PlannerApplication;
import com.zero.triptalk.planner.dto.*;
import com.zero.triptalk.planner.service.PlannerDetailService;
import com.zero.triptalk.planner.service.PlannerService;
import com.zero.triptalk.planner.type.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final PlannerService plannerService;

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

    //일정 목록 조회
    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<PlannerListResult> getPlannerList(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "6") int size,
                                                            @RequestParam SortType sortType,
                                                            Principal principal) {
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(plannerService.getPlanners(pageable, sortType));
    }

    //일정 상세페이지 조회
    @GetMapping("/{plannerId}/details")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<PlannerResponse> getPlanner(@PathVariable Long plannerId,
                                                      Principal principal) {
        return ResponseEntity.ok(plannerApplication.getPlanner(plannerId, principal.getName()));
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

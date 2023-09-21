package com.zero.triptalk.plannerdetail.controller;

import com.zero.triptalk.place.entity.PlaceRequest;
import com.zero.triptalk.plannerdetail.dto.PlannerDetailRequest;
import com.zero.triptalk.plannerdetail.dto.PlannerDetailResponse;
import com.zero.triptalk.plannerdetail.service.PlannerDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans")
public class PlannerDetailController {

    private final PlannerDetailService plannerDetailService;

    @GetMapping("/detail")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getAllPlannerDetail() {

        List<PlannerDetailResponse> list = plannerDetailService.getAllPlannerDetail();

        return ResponseEntity.ok(list);
    }

    //세부일정 요청이 한개 들어올 때
    @PostMapping("/{planId}/detail")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> createPlannerDetail(@PathVariable Long planId,
                                                 @RequestPart("files") List<MultipartFile> files,
                                                 @RequestPart PlannerDetailRequest request,
                                                 Principal principle) {

        return ResponseEntity.ok(plannerDetailService.createPlannerDetail(planId, files, request, principle.getName()));
    }

//
//    //세부일정 요청이 bulk 로 들어올 때
//    @PostMapping("/{planId}/detail")
//    @PreAuthorize("hasAuthority('USER')")
//    public ResponseEntity<?> createPlannerDetailList(@PathVariable Long planId,
//                                                     @RequestPart List<MultipartFile> files,
//                                                     @RequestPart List<PlannerDetailRequest> requests,
//                                                     Principal principal) {
//
//        return ResponseEntity.ok(plannerDetailService.createPlannerDetailList(planId, files, requests, principal.getName()));
//    }

    @PatchMapping("/{planId}/detail")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> updatePlannerDetail(@PathVariable Long planId,
                                                 @RequestPart List<MultipartFile> files,
                                                 @RequestPart PlannerDetailRequest request,
                                                 Principal principal) {

        return ResponseEntity.ok(plannerDetailService.updatePlannerDetail(files, request, principal.getName()));
    }

    @DeleteMapping("/{planId}/detail/{detailId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> deletePlannerDetail(@PathVariable Long planId,
                                                 @PathVariable Long detailId,
                                                 Principal principal) {

        return ResponseEntity.ok(plannerDetailService.deletePlannerDetail(detailId, principal.getName()));
    }
}

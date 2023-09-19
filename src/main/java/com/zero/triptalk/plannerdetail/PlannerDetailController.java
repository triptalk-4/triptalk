package com.zero.triptalk.plannerdetail;

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
    /*
    @PostMapping("/{planId}/detail")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> createPlannerDetail(@PathVariable Long planId,
                                                 @RequestPart List<MultipartFile> files,
                                                 @RequestPart PlannerDetailDto request) {

        return ResponseEntity.ok(plannerDetailService.createPlannerDetail(planId, files, request));
    }

    */
    //세부일정 요청이 bulk 로 들어올 때
    @PostMapping("/{planId}/detail")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> createPlannerDetailList(@PathVariable Long planId,
                                                    @RequestPart List<MultipartFile> files,
                                                    @RequestPart List<PlannerDetailRequest> requests) {

        return ResponseEntity.ok(plannerDetailService.createPlannerDetailList(planId, files, requests));
    }

    @PatchMapping("/{planId}/detail")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> updatePlannerDetail(@PathVariable Long planId,
                                                 @RequestPart List<MultipartFile> files,
                                                 @RequestPart PlannerDetailRequest request) {

        return ResponseEntity.ok(plannerDetailService.updatePlannerDetail(planId, files, request));
    }

    @DeleteMapping("/{planId}/detail/{detailId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> deletePlannerDetail(@PathVariable Long planId,
                                                 @PathVariable Long detailId,
                                                 Principal principal) {

        return ResponseEntity.ok(plannerDetailService.deletePlannerDetail(planId, detailId, principal.getName()));
    }
}

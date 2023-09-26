package com.zero.triptalk.plannerdetail.controller;

import com.zero.triptalk.plannerdetail.dto.PlannerDetailRequest;
import com.zero.triptalk.plannerdetail.dto.PlannerDetailListResponse;
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
    public ResponseEntity<PlannerDetailResponse> getPlannerDetail(Long plannerDetailId){
//        return ResponseEntity.ok(plannerDetailService.getPlannerDetail(plannerDetailId));
        return null;
    }


    @GetMapping("/detailList")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getAllPlannerDetail() {
        List<PlannerDetailListResponse> list = plannerDetailService.getAllPlannerDetail();
        return ResponseEntity.ok(list);
    }


    //세부일정 요청이 한개 들어올 때
    @PostMapping("/{planId}/detail")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Boolean> createPlannerDetail(@PathVariable Long planId,
                                                 @RequestPart("files") List<MultipartFile> files,
                                                 @RequestPart PlannerDetailRequest request,
                                                 Principal principle) {

        return ResponseEntity.ok(plannerDetailService.createPlannerDetail(planId, files, request, principle.getName()));
    }

    /**
     세부일정 요청이 bulk 로 들어올 때
     1. 화면에서 일정 저장 submit
     2. S3를 통해 사진 리스트를 -> url 리스트로
     3. 응답을 보내주고 상세 일정을 저장하는 request에 이미지 주소를 전달
    **/

    @PostMapping("/{planId}/detail")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Boolean> createPlannerDetailList(@PathVariable Long planId,
                                                     @RequestPart List<MultipartFile> files,
                                                     @RequestPart List<PlannerDetailRequest> requests,
                                                     Principal principal) {

        return ResponseEntity.ok(plannerDetailService.createPlannerDetailList(planId, files, requests, principal.getName()));
    }

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

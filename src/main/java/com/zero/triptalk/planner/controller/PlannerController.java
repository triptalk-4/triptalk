package com.zero.triptalk.planner.controller;

import com.zero.triptalk.application.PlannerApplication;
import com.zero.triptalk.planner.dto.PlannerDetailListRequest;
import com.zero.triptalk.planner.dto.PlannerDetailListResponse;
import com.zero.triptalk.planner.dto.PlannerDetailRequest;
import com.zero.triptalk.planner.dto.PlannerDetailResponse;
import com.zero.triptalk.planner.service.PlannerDetailService;
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
public class PlannerController {

    private final PlannerDetailService plannerDetailService;
    private final PlannerApplication plannerApplication;


    @GetMapping("/{plannerDetailId}/detail")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<PlannerDetailResponse> getPlannerDetail(@PathVariable Long plannerDetailId) {
        return ResponseEntity.ok(
                PlannerDetailResponse.from(plannerDetailService.getPlannerDetail(plannerDetailId)));
    }


    @GetMapping("/detailList")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getAllPlannerDetail() {
        List<PlannerDetailListResponse> list = plannerDetailService.getAllPlannerDetail();
        return ResponseEntity.ok(list);
    }


    //세부일정 요청이 한개 들어올 때
    @PostMapping("/{plannerId}/detail")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Boolean> createPlannerDetail(@PathVariable Long plannerId,
                                                       @RequestPart("files") List<MultipartFile> files,
                                                       @RequestPart PlannerDetailRequest request,
                                                       Principal principle) {

        return ResponseEntity.ok(plannerApplication.createPlannerDetail(plannerId, files, request, principle.getName()));
    }

    /**
     * 상세일정 저장(리스트)
     * 1. 화면에서 일정 저장 submit
     * 2. S3를 통해 사진 파일 리스트를 url 리스트로 변환하고 response
     * 3. 사진의 갯수가 맞는지 검증한 뒤
     * 4. 상세 일정을 저장하는 request에 이미지 주소를 같이 담아서 전달
     **/

    //사진 리스트 저장
    @PostMapping("/{plannerId}/images")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<String>> uploadImages(@PathVariable Long plannerId,
                                                     @RequestPart("files") List<MultipartFile> files,
                                                     Principal principal) {
        return ResponseEntity.ok(plannerDetailService.uploadImages(files));
    }


    //상세 일정 리스트 저장
    @PostMapping("/{plannerId}/detailList")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Boolean> createPlannerDetailList(@PathVariable Long plannerId,
                                                           @RequestBody List<PlannerDetailListRequest> requests,
                                                           Principal principal) {

        return ResponseEntity.ok(plannerDetailService.createPlannerDetailList(plannerId, requests, principal.getName()));
    }

    @PatchMapping("/{plannerId}/detail")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> updatePlannerDetail(@PathVariable Long plannerId,
                                                 @RequestPart List<MultipartFile> files,
                                                 @RequestPart PlannerDetailRequest request,
                                                 Principal principal) {

        return ResponseEntity.ok(plannerDetailService.updatePlannerDetail(files, request, principal.getName()));
    }

    @DeleteMapping("/{plannerId}/detail/{detailId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> deletePlannerDetail(@PathVariable Long plannerId,
                                                 @PathVariable Long detailId,
                                                 Principal principal) {

        return ResponseEntity.ok(plannerDetailService.deletePlannerDetail(detailId, principal.getName()));
    }
}

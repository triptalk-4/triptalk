package com.zero.triptalk.plannerdetail.controller;

import com.zero.triptalk.plannerdetail.dto.PlannerDetailListRequest;
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
     상세일정 저장(리스트)
     1. 화면에서 일정 저장 submit
     2. S3를 통해 사진 파일 리스트를 url 리스트로 변환하고 response
     3. 사진의 갯수가 맞는지 검증한 뒤
     4. 상세 일정을 저장하는 request에 이미지 주소를 같이 담아서 전달
    **/

    //사진 리스트 저장
    @PostMapping("/{planId}/images")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<String>> uploadImages(@PathVariable Long planId,
                                                     @RequestPart("files") List<MultipartFile> files,
                                                     Principal principal){
        return ResponseEntity.ok(plannerDetailService.uploadImages(files));
    }


    //상세 일정 리스트 저장
    @PostMapping("/{planId}/detailList")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Boolean> createPlannerDetailList(@PathVariable Long planId,
                                                     @RequestPart List<PlannerDetailListRequest> requests,
                                                     Principal principal) {

        return ResponseEntity.ok(plannerDetailService.createPlannerDetailList(planId, requests, principal.getName()));
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

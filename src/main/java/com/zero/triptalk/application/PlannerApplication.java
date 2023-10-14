package com.zero.triptalk.application;

import com.zero.triptalk.exception.code.PlannerErrorCode;
import com.zero.triptalk.exception.custom.PlannerDetailException;
import com.zero.triptalk.exception.custom.PlannerException;
import com.zero.triptalk.image.service.ImageService;
import com.zero.triptalk.like.entity.PlannerLike;
import com.zero.triptalk.like.service.LikeService;
import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.place.service.PlaceService;
import com.zero.triptalk.planner.dto.*;
import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.planner.entity.PlannerDetail;
import com.zero.triptalk.planner.service.PlannerDetailService;
import com.zero.triptalk.planner.service.PlannerService;
import com.zero.triptalk.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.zero.triptalk.exception.code.PlannerDetailErrorCode.CREATE_PLANNER_DETAIL_FAILED;
import static com.zero.triptalk.exception.code.PlannerDetailErrorCode.UNMATCHED_USER_PLANNER;

@Service
@RequiredArgsConstructor
public class PlannerApplication {

    private final PlannerService plannerService;

    private final PlannerDetailService plannerDetailService;

    private final PlaceService placeService;

    private final ImageService imageService;

    private final LikeService likeService;

    //상세 일정 한개 생성
    @Transactional
    public boolean createPlannerDetail(Long plannerId, List<MultipartFile> files,
                                       PlannerDetailRequest request, String email) {

        UserEntity user = plannerDetailService.findByEmail(email);
        Planner planner = plannerService.findById(plannerId);

        //place 저장
        Optional<Place> byRoadAddress = placeService.findByRoadAddress(request.getPlaceInfo().getRoadAddress());
        Place place = byRoadAddress.orElseGet(
                () -> placeService.savePlace(request.getPlaceInfo()));
        //S3 -> url 리스트 변환
        List<String> images = imageService.uploadFiles(files);

        //상세 일정 저장
        PlannerDetail plannerDetail = PlannerDetail.buildPlannerDetail(
                planner, request, user, place, images);
        plannerDetailService.savePlannerDetail(plannerDetail);

        return true;
    }

    //일정 생성(일정 정보와 상세일정 리스트 모두 저장)
    @Transactional
    public boolean createPlanner(PlannerRequest plannerRequest, List<PlannerDetailListRequest> requests, String email) {

        try {
            UserEntity user = plannerDetailService.findByEmail(email);
            //썸네일 가져오기 (첫번째 상세일정 첫번째 사진, 사진 업로드가 필수라 가능함)
            // (만약 사진이 필수가 아니라면 stream 으로 사진을 찾던가 추천사진?)
            String thumbnail = requests.get(0).getImages().get(0);

            //일정 생성
            Planner planner = plannerService.createPlanner(plannerRequest, user, thumbnail);

            //상세 일정 저장
            List<PlannerDetail> detailList = requests.stream().map(request -> {
                Optional<Place> byRoadAddress = placeService.findByRoadAddress(request.getPlaceInfo().getRoadAddress());
                Place place = byRoadAddress.orElseGet(
                        () -> placeService.savePlace(request.getPlaceInfo()));
                return request.toEntity(planner, place, user.getUserId());
            }).collect(Collectors.toList());

            plannerDetailService.savePlannerDetailList(detailList);

        } catch (PlannerDetailException e) {
            throw new PlannerDetailException(CREATE_PLANNER_DETAIL_FAILED);
        }
        return true;
    }

    //상세 일정 삭제
    @Transactional
    public void deletePlannerDetail(Long plannerDetailId, String email) {

        UserEntity user = plannerDetailService.findByEmail(email);
        PlannerDetail plannerDetail = plannerDetailService.findById(plannerDetailId);

        if (!user.getUserId().equals(plannerDetail.getUserId())) {
            throw new PlannerDetailException(UNMATCHED_USER_PLANNER);
        }
        imageService.deleteFiles(plannerDetail.getImages());

        plannerDetailService.deletePlannerDetail(plannerDetailId);
    }

    // 일정 삭제
    @Transactional
    public void deletePlanner(Long plannerId, String email) {

        //일정에 존재하는 상세 일정 모두 조회해서 삭제
        List<PlannerDetail> byPlanner = plannerDetailService.findByPlannerId(plannerId);
        byPlanner.forEach(details -> deletePlannerDetail(details.getPlannerDetailId(), email));

        //일정 삭제
        plannerService.deletePlanner(plannerId);
    }

    //일정 상세페이지 조회
    public PlannerResponse getPlanner(Long plannerId, String email) {

        UserEntity user = plannerDetailService.findByEmail(email);

        PlannerLike plannerLike = likeService.findByPlannerId(plannerId);
        Long likeCount = (plannerLike != null) ? plannerLike.getLikeCount() : 0;

        Planner planner = plannerService.findById(plannerId);

        planner.increaseViews();
        List<PlannerDetailResponse> responses = plannerDetailService.findByPlannerId(plannerId).stream().map(
                PlannerDetailResponse::from).collect(Collectors.toList());

        return PlannerResponse.of(planner, user, responses, likeCount);
    }

    //일정 수정
    @Transactional
    public boolean updatePlanner(Long plannerId, UpdatePlannerInfo info, String email) {
        UserEntity byEmail = plannerDetailService.findByEmail(email);
        Planner planner = plannerService.findById(plannerId);
        try {
            planner.updatePlanner(info.getPlannerRequest());
            List<PlannerDetail> result = info.getPlannerDetailListRequests().stream().map(
                    request -> {
                        Optional<Place> byRoadAddress = placeService.findByRoadAddress(request.getPlaceInfo().getRoadAddress());
                        Place place = byRoadAddress.orElseGet(
                                () -> placeService.savePlace(request.getPlaceInfo())
                        );
                        return request.toEntity(planner, place, byEmail.getUserId());
                    }).collect(Collectors.toList());
            plannerDetailService.savePlannerDetailList(result);
        } catch (Exception e) {
            throw new PlannerException(PlannerErrorCode.UPDATE_PLANNER_FAILED);
        }

        return true;
    }
}

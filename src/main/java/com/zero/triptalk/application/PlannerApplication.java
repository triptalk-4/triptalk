package com.zero.triptalk.application;

import com.zero.triptalk.exception.code.PlannerErrorCode;
import com.zero.triptalk.exception.type.PlannerDetailException;
import com.zero.triptalk.exception.type.PlannerException;
import com.zero.triptalk.exception.type.UserException;
import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.place.service.ImageService;
import com.zero.triptalk.place.service.PlaceService;
import com.zero.triptalk.planner.dto.PlannerDetailListRequest;
import com.zero.triptalk.planner.dto.PlannerDetailRequest;
import com.zero.triptalk.planner.dto.PlannerRequest;
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
import java.util.stream.Collectors;

import static com.zero.triptalk.exception.code.PlannerDetailErrorCode.*;
import static com.zero.triptalk.exception.code.UserErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PlannerApplication {

    private final PlannerService plannerService;

    private final PlannerDetailService plannerDetailService;

    private final PlaceService placeService;

    private final ImageService imageService;

    //상세 일정 한개 생성
    @Transactional
    public boolean createPlannerDetail(Long plannerId, List<MultipartFile> files,
                                       PlannerDetailRequest request, String email) {

        UserEntity user = plannerDetailService.findByEmail(email);
        Planner planner = plannerService.findById(plannerId);

        //place 저장
        Place place = placeService.savePlace(request.getPlaceInfo());
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
            //일정 생성
            Planner planner = plannerService.createPlanner(plannerRequest);

            //상세 일정 저장
            List<PlannerDetail> detailList = requests.stream().map(request -> {
                Place place = placeService.savePlace(request.getPlaceInfo());
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
        imageService.deleteImages(plannerDetail.getImages());

        plannerDetailService.deletePlannerDetail(plannerDetailId);
    }

    // 일정 삭제
    @Transactional
    public void deletePlanner(Long plannerId, String email) {

        UserEntity user = plannerDetailService.findByEmail(email);
        Planner planner = plannerService.findById(plannerId);
        if (!planner.getUser().equals(user)){
            throw new PlannerException(PlannerErrorCode.UNMATCHED_USER_PLANNER);
        }

        //일정에 존재하는 상세 일정 모두 조회해서 삭제
        List<PlannerDetail> byPlanner = plannerDetailService.findByPlanner(planner);
        byPlanner.forEach(details -> deletePlannerDetail(details.getId(),email));

        //일정 삭제
        plannerService.deletePlanner(plannerId);
    }
}

package com.zero.triptalk.plannerdetail.service;

import com.zero.triptalk.exception.code.PlannerErrorCode;
import com.zero.triptalk.exception.type.PlannerDetailException;
import com.zero.triptalk.exception.type.PlannerException;
import com.zero.triptalk.exception.type.UserException;
import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.place.service.ImageService;
import com.zero.triptalk.place.service.PlaceService;
import com.zero.triptalk.plannerdetail.dto.PlannerDetailDto;
import com.zero.triptalk.plannerdetail.dto.PlannerDetailListRequest;
import com.zero.triptalk.plannerdetail.dto.PlannerDetailListResponse;
import com.zero.triptalk.plannerdetail.dto.PlannerDetailRequest;
import com.zero.triptalk.plannerdetail.entity.Planner;
import com.zero.triptalk.plannerdetail.entity.PlannerDetail;
import com.zero.triptalk.plannerdetail.repository.PlannerDetailRepository;
import com.zero.triptalk.plannerdetail.repository.PlannerRepository;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.repository.UserRepository;
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
public class PlannerDetailService {

    private final PlannerDetailRepository plannerDetailRepository;
    private final PlannerRepository plannerRepository;

    private final UserRepository userRepository;
    private final PlaceService placeService;
    private final ImageService imageService;


    public List<PlannerDetailListResponse> getAllPlannerDetail() {

        List<PlannerDetail> detailList = plannerDetailRepository.findAll();

        return PlannerDetailListResponse.of(detailList);
    }

    public PlannerDetailDto getPlannerDetail(Long plannerDetailId) {
        PlannerDetail plannerDetail = plannerDetailRepository.findById(plannerDetailId).orElseThrow(
                () -> new PlannerDetailException(NOT_FOUND_PLANNER_DETAIL)
        );
        return PlannerDetailDto.ofEntity(plannerDetail);
    }

    @Transactional
    public boolean createPlannerDetail(Long plannerId, List<MultipartFile> files,
                                       PlannerDetailRequest request, String email) {

        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserException(USER_NOT_FOUND));

        Planner planner = plannerRepository.findById(plannerId).orElseThrow(
                () -> new PlannerException(PlannerErrorCode.NOT_FOUND_PLANNER)
        );

        //place 저장
        Place place = placeService.savePlace(request.getPlaceInfo());
        //S3 -> url 리스트 변환
        List<String> images = imageService.uploadFiles(files);
        //상세 일정 저장
        PlannerDetail plannerDetail = PlannerDetail.buildPlannerDetail(
                planner, request, user, place, images);
        plannerDetailRepository.save(plannerDetail);

        return true;
    }

    public List<String> uploadImages(List<MultipartFile> files) {
        return imageService.uploadFiles(files);
    }


    @Transactional
    public boolean createPlannerDetailList(Long plannerId, List<PlannerDetailListRequest> requests, String email) {

        try {

            UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                    new UserException(USER_NOT_FOUND));

            Planner planner = plannerRepository.findById(plannerId).orElseThrow(
                    () -> new PlannerException(PlannerErrorCode.NOT_FOUND_PLANNER)
            );

            List<PlannerDetail> detailList = requests.stream().map(request -> {
                Place place = placeService.savePlace(request.getPlaceInfo());
                return request.toEntity(planner, place, user.getUserId());
            }).collect(Collectors.toList());

            if (requests.size() != detailList.size()) {
                throw new PlannerDetailException(CREATE_PLANNER_DETAIL_FAILED);
            }

            plannerDetailRepository.saveAll(detailList);

        } catch (PlannerDetailException e) {
            throw new PlannerDetailException(CREATE_PLANNER_DETAIL_FAILED);
        }
        return true;
    }

    public boolean updatePlannerDetail(List<MultipartFile> files,
                                       PlannerDetailRequest request, String email) {

        // file 검증

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserException(USER_NOT_FOUND));

        PlannerDetail plannerDetail = plannerDetailRepository.findById(request.getId()).orElseThrow(() ->
                new PlannerDetailException(NOT_FOUND_PLANNER_DETAIL));

        if (!user.getUserId().equals(plannerDetail.getUserId())) {
            throw new PlannerDetailException(UNMATCHED_USER_PLANNER);
        }

        //장소 업데이트

        plannerDetail.updatePlannerDetail(request);

        plannerDetailRepository.save(plannerDetail);

        return true;
    }

    public boolean deletePlannerDetail(Long detailId, String email) {

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserException(USER_NOT_FOUND));

        PlannerDetail plannerDetail = plannerDetailRepository.findById(detailId)
                .orElseThrow(() -> new PlannerDetailException(NOT_FOUND_PLANNER_DETAIL));

        if (!user.getUserId().equals(plannerDetail.getUserId())) {
            throw new PlannerDetailException(UNMATCHED_USER_PLANNER);
        }

        plannerDetailRepository.deleteById(plannerDetail.getId());

        return true;
    }


}

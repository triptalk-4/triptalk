package com.zero.triptalk.planner.service;

import com.zero.triptalk.exception.type.PlannerDetailException;
import com.zero.triptalk.exception.type.UserException;
import com.zero.triptalk.place.service.ImageService;
import com.zero.triptalk.place.service.PlaceService;
import com.zero.triptalk.planner.dto.PlannerDetailListResponse;
import com.zero.triptalk.planner.dto.PlannerDetailRequest;
import com.zero.triptalk.planner.dto.PlannerDetailResponse;
import com.zero.triptalk.planner.entity.PlannerDetail;
import com.zero.triptalk.planner.repository.PlannerDetailRepository;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.zero.triptalk.exception.code.PlannerDetailErrorCode.NOT_FOUND_PLANNER_DETAIL;
import static com.zero.triptalk.exception.code.PlannerDetailErrorCode.UNMATCHED_USER_PLANNER;
import static com.zero.triptalk.exception.code.UserErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PlannerDetailService {

    private final PlannerDetailRepository plannerDetailRepository;

    private final UserRepository userRepository;
    private final PlaceService placeService;
    private final ImageService imageService;


    public List<PlannerDetailListResponse> getAllPlannerDetail() {

        List<PlannerDetail> detailList = plannerDetailRepository.findAll();

        return PlannerDetailListResponse.of(detailList);
    }

    public PlannerDetailResponse getPlannerDetail(Long plannerDetailId) {
        PlannerDetail plannerDetail = plannerDetailRepository.findById(plannerDetailId).orElseThrow(
                () -> new PlannerDetailException(NOT_FOUND_PLANNER_DETAIL)
        );
        return PlannerDetailResponse.from(plannerDetail);
    }

    //userService 에서 합칠예정
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserException(USER_NOT_FOUND));
    }

    public List<String> uploadImages(List<MultipartFile> files) {
        return imageService.uploadFiles(files);
    }

    public void savePlannerDetail(PlannerDetail plannerDetail) {
        plannerDetailRepository.save(plannerDetail);
    }

    public void savePlannerDetailList(List<PlannerDetail> plannerDetailList) {
        plannerDetailRepository.saveAll(plannerDetailList);
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

    public void deletePlannerDetail(Long plannerDetailId, String email) {

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserException(USER_NOT_FOUND));

        PlannerDetail plannerDetail = plannerDetailRepository.findById(plannerDetailId)
                .orElseThrow(() -> new PlannerDetailException(NOT_FOUND_PLANNER_DETAIL));

        if (!user.getUserId().equals(plannerDetail.getUserId())) {
            throw new PlannerDetailException(UNMATCHED_USER_PLANNER);
        }

        plannerDetailRepository.deleteById(plannerDetail.getId());

    }


}

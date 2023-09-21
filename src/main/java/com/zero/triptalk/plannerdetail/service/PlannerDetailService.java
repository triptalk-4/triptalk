package com.zero.triptalk.plannerdetail.service;

import com.zero.triptalk.exception.type.PlannerDetailException;
import com.zero.triptalk.exception.type.UserException;
import com.zero.triptalk.place.entity.Images;
import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.place.service.ImageService;
import com.zero.triptalk.place.service.PlaceService;
import com.zero.triptalk.plannerdetail.dto.PlannerDetailRequest;
import com.zero.triptalk.plannerdetail.dto.PlannerDetailResponse;
import com.zero.triptalk.plannerdetail.entity.PlannerDetail;
import com.zero.triptalk.plannerdetail.repository.PlannerDetailRepository;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.zero.triptalk.exception.code.PlannerDetailErrorCode.PLANNER_DETAIL_NOT_FOUNT;
import static com.zero.triptalk.exception.code.PlannerDetailErrorCode.UNMATCHED_USER_PLANNER;
import static com.zero.triptalk.exception.code.UserErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PlannerDetailService {

    private final PlannerDetailRepository plannerDetailRepository;
    private final UserRepository userRepository;
    private final PlaceService placeService;
    private final ImageService imageService;


    public List<PlannerDetailResponse> getAllPlannerDetail() {

        List<PlannerDetail> detailList = plannerDetailRepository.findAll();

        return PlannerDetailResponse.of(detailList);
    }

    public boolean createPlannerDetail(Long planId, List<MultipartFile> files,
                                       PlannerDetailRequest request, String email) {

        // file 검증

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserException(USER_NOT_FOUND));
        //place 저장
        Place place = placeService.savePlace(request.getPlaceInfo());
        //사진 저장
        List<Images> uploadFiles = imageService.uploadFiles(files);

        PlannerDetail plannerDetail = PlannerDetail.builder()
                .plannerId(planId)
                .userId(user.getUserId())
                .date(request.getDate())
                .time(request.getTime())
                .image(request.getImage())
                .description(request.getDescription())
                .place(place)
                .images(uploadFiles)
                .build();
        plannerDetailRepository.save(plannerDetail);

        return true;
    }

    public boolean createPlannerDetailList(Long planId, List<MultipartFile> files,
                                           List<PlannerDetailRequest> requests, String email) {

        // file 검증

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserException(USER_NOT_FOUND));

        List<PlannerDetail> detailList = new ArrayList<>();
        for (PlannerDetailRequest x : requests) {

            PlannerDetail plannerDetail = PlannerDetail.builder()
                    .plannerId(planId)
                    .userId(user.getUserId())
                    .date(x.getDate())
                    .time(x.getTime())
                    .image(x.getImage())
                    .description(x.getDescription())
                    .build();
            detailList.add(plannerDetail);
        }
        plannerDetailRepository.saveAll(detailList);

        return true;
    }

    public boolean updatePlannerDetail(List<MultipartFile> files,
                                       PlannerDetailRequest request, String email) {

        // file 검증

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserException(USER_NOT_FOUND));

        PlannerDetail plannerDetail = plannerDetailRepository.findById(request.getId()).orElseThrow(() ->
                new PlannerDetailException(PLANNER_DETAIL_NOT_FOUNT));

        if (!user.getUserId().equals(plannerDetail.getUserId())) {
            throw new PlannerDetailException(UNMATCHED_USER_PLANNER);
        }

        plannerDetail.updatePlannerDetail(request);
        plannerDetailRepository.save(plannerDetail);

        return true;
    }

    public boolean deletePlannerDetail(Long detailId, String email) {

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserException(USER_NOT_FOUND));

        PlannerDetail plannerDetail = plannerDetailRepository.findById(detailId)
                .orElseThrow(() -> new PlannerDetailException(PLANNER_DETAIL_NOT_FOUNT));

        if (!user.getUserId().equals(plannerDetail.getUserId())) {
            throw new PlannerDetailException(UNMATCHED_USER_PLANNER);
        }

        plannerDetailRepository.deleteById(plannerDetail.getId());

        return true;
    }
}

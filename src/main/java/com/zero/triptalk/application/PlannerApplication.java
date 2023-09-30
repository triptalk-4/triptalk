package com.zero.triptalk.application;

import com.zero.triptalk.exception.code.PlannerErrorCode;
import com.zero.triptalk.exception.type.PlannerException;
import com.zero.triptalk.exception.type.UserException;
import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.place.service.ImageService;
import com.zero.triptalk.place.service.PlaceService;
import com.zero.triptalk.plannerdetail.dto.PlannerDetailRequest;
import com.zero.triptalk.plannerdetail.entity.Planner;
import com.zero.triptalk.plannerdetail.entity.PlannerDetail;
import com.zero.triptalk.plannerdetail.service.PlannerDetailService;
import com.zero.triptalk.plannerdetail.service.PlannerService;
import com.zero.triptalk.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
}

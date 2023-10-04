package com.zero.triptalk.like.service;

import com.zero.triptalk.exception.code.LikeErrorCode;
import com.zero.triptalk.exception.custom.LikeException;
import com.zero.triptalk.like.dto.response.LikenOnePlusResponse;
import com.zero.triptalk.like.entity.DetailPlannerLike;
import com.zero.triptalk.like.repository.DetailPlannerLikeRepository;
import com.zero.triptalk.planner.entity.PlannerDetail;
import com.zero.triptalk.planner.repository.PlannerDetailRepository;
import com.zero.triptalk.planner.repository.PlannerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.zero.triptalk.exception.code.LikeErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {

    private final PlannerDetailRepository plannerDetailRepository;
    private final DetailPlannerLikeRepository detailPlannerLikeRepository;

    private final PlannerRepository plannerRepository;
    public Object createPlannerDetail(Long plannerId) {
        PlannerDetail plannerDetail = plannerDetailRepository.findById(plannerId)
                .orElseThrow(() -> new LikeException(NO_Planner_Detail_Board));

        // DetailPlannerLike를 찾습니다.
        Optional<DetailPlannerLike> detailPlannerLikeOptional = detailPlannerLikeRepository.findByPlannerDetail(plannerDetail);

        // 기존 DetailPlannerLike가 없으면 새로 생성합니다.
        DetailPlannerLike detailPlannerLike;
        if (detailPlannerLikeOptional.isEmpty()) {
            detailPlannerLike = DetailPlannerLike.builder()
                    .plannerDetail(plannerDetail)
                    .likeCount(1.0) // 좋아요 수를 1로 초기화합니다.
                    .build();
            detailPlannerLikeRepository.save(detailPlannerLike);
        }
        detailPlannerLike = detailPlannerLikeOptional.get();
        // 기존 좋아요 수를 증가시킵니다.
        double currentLikeCount = detailPlannerLike.getLikeCount();
        double newLikeCount = currentLikeCount + 1;

        detailPlannerLike.setLikeCount(newLikeCount);


        // DetailPlannerLike를 저장합니다.
        detailPlannerLikeRepository.save(detailPlannerLike);

        // 이후 추가 작업을 수행하거나 필요한 반환값을 반환할 수 있습니다.
        // ...
        return LikenOnePlusResponse.builder()
                .ok("좋아요가 완료되었습니다")
                .build();

    }
}

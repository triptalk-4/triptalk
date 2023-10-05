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
    public Object createPlannerDetail(Long plannerDetailId) {
        PlannerDetail plannerDetail = plannerDetailRepository.findById(plannerDetailId)
                .orElseThrow(() -> new LikeException(NO_Planner_Detail_Board));

        DetailPlannerLike detailPlannerLike = detailPlannerLikeRepository.findByPlannerDetail(plannerDetail);
        if (detailPlannerLike == null) {
            // 기존 DetailPlannerLike가 없으면 새로 생성합니다.
            detailPlannerLike = DetailPlannerLike.builder()
                    .plannerDetail(plannerDetail)
                    .likeCount(1.0) // 좋아요 수를 1로 초기화합니다.
                    .build();
            detailPlannerLikeRepository.save(detailPlannerLike);
        }
        // 기존 좋아요 수를 증가시킵니다.
        double currentLikeCount = detailPlannerLike.getLikeCount();
        double newLikeCount = currentLikeCount + 1;
        detailPlannerLike.setLikeCount(newLikeCount);
        detailPlannerLikeRepository.save(detailPlannerLike);


        return LikenOnePlusResponse.builder()
                .ok("좋아요가 완료되었습니다")
                .build();

    }
}

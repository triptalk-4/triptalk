package com.zero.triptalk.like.service;

import com.zero.triptalk.exception.code.LikeErrorCode;
import com.zero.triptalk.exception.custom.LikeException;
import com.zero.triptalk.like.dto.response.LikenOnePlusMinusResponse;
import com.zero.triptalk.like.entity.DetailPlannerLike;
import com.zero.triptalk.like.repository.DetailPlannerLikeRepository;
import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.planner.entity.PlannerDetail;
import com.zero.triptalk.planner.repository.PlannerDetailRepository;
import com.zero.triptalk.planner.repository.PlannerRepository;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.zero.triptalk.exception.code.LikeErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {

    private final PlannerDetailRepository plannerDetailRepository;
    private final DetailPlannerLikeRepository detailPlannerLikeRepository;
    private final UserRepository userRepository;

    private final PlannerRepository plannerRepository;

    public String userNickname(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = "기본 이메일";
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            email = userDetails.getUsername(); // 사용자 이메일 정보를 추출
        }

        // System.out.println("email: " + email);

        String nickname = "기본 닉네임";
        Optional<UserEntity> existingEmail = userRepository.findByEmail(email);

        if (existingEmail.isPresent()) {
            UserEntity user = existingEmail.get();
            System.out.println(user);
            nickname = user.getNickname(); // 사용자의 닉네임을 얻음
        }

        return nickname;
    }
    public Object createPlannerDetail(Long plannerDetailId) {
        PlannerDetail plannerDetail = plannerDetailRepository.findById(plannerDetailId)
                .orElseThrow(() -> new LikeException(NO_Planner_Detail_Board));
        Planner planner = plannerDetail.getPlanner();
        System.out.println("test"+planner.toString());


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


        return LikenOnePlusMinusResponse.builder()
                .ok("좋아요가 완료되었습니다")
                .build();

    }

    public Object LikeOneMinus(Long plannerDetailId) {
        PlannerDetail plannerDetail = plannerDetailRepository.findById(plannerDetailId)
                .orElseThrow(() -> new LikeException(NO_Planner_Detail_Board));

        DetailPlannerLike detailPlannerLike = detailPlannerLikeRepository.findByPlannerDetail(plannerDetail);


        double currentLikeCount = detailPlannerLike.getLikeCount();
        double newLikeCount = currentLikeCount - 1;

        detailPlannerLike.setLikeCount(newLikeCount);
        detailPlannerLikeRepository.save(detailPlannerLike);

        return LikenOnePlusMinusResponse.builder()
                .ok("좋아요가 취소되었습니다")
                .build();
    }
}

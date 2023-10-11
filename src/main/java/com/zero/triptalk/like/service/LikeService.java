package com.zero.triptalk.like.service;

import com.zero.triptalk.exception.code.LikeErrorCode;
import com.zero.triptalk.exception.code.UserErrorCode;
import com.zero.triptalk.exception.custom.LikeException;
import com.zero.triptalk.exception.custom.UserException;
import com.zero.triptalk.like.dto.response.LikenOnePlusMinusResponse;
import com.zero.triptalk.like.entity.DetailPlannerLike;
import com.zero.triptalk.like.entity.PlannerLike;
import com.zero.triptalk.like.entity.UserLikeEntity;
import com.zero.triptalk.like.repository.DetailPlannerLikeRepository;
import com.zero.triptalk.like.repository.PlannerLikeRepository;
import com.zero.triptalk.like.repository.UserLikeRepository;
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

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.zero.triptalk.exception.code.LikeErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {

    private final PlannerDetailRepository plannerDetailRepository;
    private final DetailPlannerLikeRepository detailPlannerLikeRepository;
    private final UserRepository userRepository;
    private final UserLikeRepository userLikeRepository;

    private final PlannerLikeRepository plannerLikeRepository;

    /**
     * 토큰 값안의 이메일 불러오기
     * @return
     */
    public String userEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = "기본 이메일";
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            email = userDetails.getUsername(); // 사용자 이메일 정보를 추출
        }

        return email;
    }
    @Transactional
    public Object createLikeOrPlusPlannerDetail(Long plannerDetailId) {
        PlannerDetail plannerDetail = plannerDetailRepository.findById(plannerDetailId)
                .orElseThrow(() -> new LikeException(LikeErrorCode.NO_Planner_Detail_Board));

        LocalDateTime currentTime = LocalDateTime.now();

        // 좋아요를 누른 접속자 유저 찾기
        String email = userEmail(); // 이메일 불러오기
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.EMAIL_NOT_FOUND_ERROR));

        // 이미 좋아요를 한 경우 처리
        if (userLikeRepository.existsByPlannerDetailAndUser(plannerDetail, userEntity)) {
            throw new LikeException(LikeErrorCode.NO_LIKE_DUPLICATE_ERROR);
        }

        // 좋아요  한 유저 저장
        UserLikeEntity userLike = UserLikeEntity.builder()
                .plannerDetail(plannerDetail)
                .planner(plannerDetail.getPlanner())
                .likeDt(currentTime)
                .user(userEntity)
                .build();
        userLikeRepository.save(userLike);

        // DetailPlannerLike 업데이트 또는 생성
        DetailPlannerLike detailPlannerLike = detailPlannerLikeRepository.findByPlannerDetail(plannerDetail);
        PlannerLike plannerLike = plannerLikeRepository.findByPlanner(plannerDetail.getPlanner());
        if (detailPlannerLike == null) {
            // detailPlannerLike 추가
            detailPlannerLike = DetailPlannerLike.builder()
                    .plannerDetail(plannerDetail)
                    .likeCount(1L)
                    .likeDt(currentTime)
                    .build();
            detailPlannerLikeRepository.save(detailPlannerLike);

        }

        if( plannerLike == null){
            plannerLike = PlannerLike.builder()
                    .planner(plannerDetail.getPlanner())
                    .likeCount((long) 1.0)
                    .likeDt(currentTime)
                    .build();

            plannerLikeRepository.save(plannerLike);

            return LikenOnePlusMinusResponse.builder()
                    .ok("좋아요가 완료되었습니다")
                    .build();
        }
        // detailPlannerLike 추가
        long currentDetailLikeCount = detailPlannerLike.getLikeCount();
        long newDetailLikeCount = currentDetailLikeCount + 1;
        detailPlannerLike.setLikeCount(newDetailLikeCount);
        // plannerLike 추가
        long currentPlannerLikeCount = plannerLike.getLikeCount();
        long newPlannerLikeCount = currentPlannerLikeCount + 1;
        plannerLike.setLikeCount(newPlannerLikeCount);

        detailPlannerLikeRepository.save(detailPlannerLike);
        plannerLikeRepository.save(plannerLike);

        return LikenOnePlusMinusResponse.builder()
                .ok("좋아요가 완료되었습니다")
                .plannerCount(newPlannerLikeCount)
                .detailPlannerCount(newDetailLikeCount)
                .build();
    }

    @Transactional
    public Object LikeOneMinus(Long plannerDetailId) {
        PlannerDetail plannerDetail = plannerDetailRepository.findById(plannerDetailId)
                .orElseThrow(() -> new LikeException(NO_Planner_Detail_Board));
        // 좋아요 취소 (플레너 디테일 라이크)
        DetailPlannerLike detailPlannerLike = detailPlannerLikeRepository.findByPlannerDetail(plannerDetail);
        //플레너 디테일 좋아요취소
        long currentDetailLikeCount = detailPlannerLike.getLikeCount();
        long newDetailLikeCount = currentDetailLikeCount - 1;

        detailPlannerLike.setLikeCount(newDetailLikeCount);
        detailPlannerLikeRepository.save(detailPlannerLike);

        //좋아요 취소 (일정 좋아요 취소)
        PlannerLike plannerLike = plannerLikeRepository.findByPlanner(plannerDetail.getPlanner());

        long currentPlanerLikeCount = plannerLike.getLikeCount();
        long newPlannerLikeCount = currentPlanerLikeCount -1;

        plannerLike.setLikeCount(newPlannerLikeCount);
        plannerLikeRepository.save(plannerLike);

        // 좋아요를 누른 접속자 유저 찾기
        String email = userEmail(); // 이메일 불러오기
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.EMAIL_NOT_FOUND_ERROR));

        // 좋아요 취소 하면 등록 취소 
        UserLikeEntity userLike = (UserLikeEntity) userLikeRepository.findByPlannerDetailAndUser(plannerDetail,user)
                .orElseThrow(() -> new LikeException(LikeErrorCode.NO_LIKE_SEARCH_ERROR));

        userLikeRepository.delete(userLike);

        return LikenOnePlusMinusResponse.builder()
                .ok("좋아요가 취소되었습니다")
                .plannerCount(newPlannerLikeCount)
                .detailPlannerCount(newDetailLikeCount)
                .build();
    }

    public PlannerLike findByPlannerId(Long plannerId) {
        return plannerLikeRepository.findByPlanner_PlannerId(plannerId);
    }
}

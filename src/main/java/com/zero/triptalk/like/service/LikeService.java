package com.zero.triptalk.like.service;

import com.zero.triptalk.exception.code.LikeErrorCode;
import com.zero.triptalk.exception.code.UserErrorCode;
import com.zero.triptalk.exception.custom.LikeException;
import com.zero.triptalk.exception.custom.UserException;
import com.zero.triptalk.like.dto.response.LikenOnePlusMinusResponse;
import com.zero.triptalk.like.dto.response.UserLikeAndSaveYnResponse;
import com.zero.triptalk.like.dto.response.UserSaveAndCancelResponse;
import com.zero.triptalk.like.entity.PlannerLike;
import com.zero.triptalk.like.entity.UserLikeEntity;
import com.zero.triptalk.like.entity.UserSave;
import com.zero.triptalk.like.repository.PlannerLikeRepository;
import com.zero.triptalk.like.repository.UserLikeRepository;
import com.zero.triptalk.like.repository.UserSaveRepository;
import com.zero.triptalk.planner.entity.Planner;
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

import static com.zero.triptalk.exception.code.LikeErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {

    private final PlannerRepository plannerRepository;
    private final UserSaveRepository userSaveRepository;
    private final PlannerLikeRepository plannerLikeRepository;
    private final UserRepository userRepository;
    private final UserLikeRepository userLikeRepository;

    /**
     * 토큰 값안의 이메일 불러오기
     * @return
     */
    private String userEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = "기본 이메일";
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            email = userDetails.getUsername(); // 사용자 이메일 정보를 추출
        }

        return email;
    }
    @Transactional
    public LikenOnePlusMinusResponse createLikeOrPlusPlanner(Long plannerId) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new LikeException(NO_Planner_Detail_Board));

        // DetailPlannerLike 업데이트 또는 생성
        PlannerLike plannerLike = plannerLikeRepository.findByPlanner(planner);

        String email = userEmail(); // 이메일 불러오기
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.EMAIL_NOT_FOUND_ERROR));

        // 이미 좋아요를 한 경우 처리
        if (userLikeRepository.existsByPlannerAndUser(planner, user)) {
            throw new LikeException(LikeErrorCode.NO_LIKE_DUPLICATE_ERROR);
        }

        // 좋아요 한 유저 저장
        UserLikeEntity userLike = UserLikeEntity.builder()
                .planner(planner)
                .likeDt(LocalDateTime.now())
                .user(user)
                .build();
        userLikeRepository.save(userLike);

        if( plannerLike == null){
            plannerLike = PlannerLike.builder()
                    .planner(planner)
                    .likeCount((long) 1.0)
                    .likeDt(LocalDateTime.now())
                    .build();

            plannerLikeRepository.save(plannerLike);

            return LikenOnePlusMinusResponse.builder()
                    .ok("좋아요가 완료되었습니다")
                    .plannerCount(1L)
                    .build();
        }
        // plannerLike 추가
        long currentPlannerLikeCount = plannerLike.getLikeCount();
        long newPlannerLikeCount = currentPlannerLikeCount + 1;
        plannerLike.setLikeCount(newPlannerLikeCount);

        plannerLikeRepository.save(plannerLike);

        return LikenOnePlusMinusResponse.builder()
                .ok("좋아요가 완료되었습니다")
                .plannerCount(newPlannerLikeCount)
                .build();
    }

    @Transactional
    public LikenOnePlusMinusResponse LikeOneMinus(Long plannerDetailId) {
        Planner planner = plannerRepository.findById(plannerDetailId)
                .orElseThrow(() -> new LikeException(NO_Planner_Detail_Board));

        PlannerLike plannerLike = plannerLikeRepository.findByPlanner(planner);

        //플레너 디테일 좋아요취소
        long currentDetailLikeCount = plannerLike.getLikeCount();
        long newLikeCount = currentDetailLikeCount - 1;

        plannerLike.setLikeCount(newLikeCount);

        plannerLikeRepository.save(plannerLike);

        // 좋아요를 누른 접속자 유저 찾기
        String email = userEmail(); // 이메일 불러오기
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.EMAIL_NOT_FOUND_ERROR));

        // 좋아요 취소 하면 등록 취소
        UserLikeEntity userLike = (UserLikeEntity) userLikeRepository.findByPlannerAndUser(planner,user)
                .orElseThrow(() -> new LikeException(LikeErrorCode.NO_LIKE_SEARCH_ERROR));

        userLikeRepository.delete(userLike);

        return LikenOnePlusMinusResponse.builder()
                .ok("좋아요가 취소되었습니다")
                .plannerCount(newLikeCount)
                .build();
    }

    public PlannerLike findByPlannerId(Long plannerId) {
        return plannerLikeRepository.findByPlanner_PlannerId(plannerId);
    }

    public UserSaveAndCancelResponse userSavePlus(Long plannerId) {
        // 게시글 찾기
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new LikeException(NO_Planner_Detail_Board));

        String userEmail = userEmail();

        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new LikeException(No_User_Search));

        UserSave userSave = userSaveRepository.findByPlannerAndUser(planner,user);

        if(userSave != null){
            throw new LikeException(LikeErrorCode.NO_SAVE_DUPLICATE_ERROR);
        }


        if(userSave == null){
            UserSave userSaveFin = UserSave.builder()
                    .planner(planner)
                    .user(user)
                    .saveDt(LocalDateTime.now())
                    .build();

            userSaveRepository.save(userSaveFin);
        }
        return UserSaveAndCancelResponse.builder()
                .ok("저장 추가가 완료되었습니다.")
                .build();
    }

    public UserSaveAndCancelResponse userCancel(Long plannerId) {

        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new LikeException(NO_Planner_Detail_Board));

        String userEmail = userEmail();

        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new LikeException(No_User_Search));

        UserSave userSaveDelete = userSaveRepository.findByPlannerAndUser(planner,user);

        if(userSaveDelete == null) {
            throw new LikeException(NO_SAVE_EXIST_ERROR);
        }else {

            userSaveRepository.delete(userSaveDelete);

            return UserSaveAndCancelResponse.builder()
                    .ok("저장함 삭제가 완료되었습니다.")
                    .build();
        }
    }

    public UserLikeAndSaveYnResponse userCheckYn(Long plannerId) {

        String userSaveYn = "no";
        String userLikeYn = "no";

        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new LikeException(NO_Planner_Detail_Board));

        String userEmail = userEmail();

        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new LikeException(No_User_Search));

        boolean userSaveYnCheck = userSaveRepository.existsByPlannerAndUser(planner,user);
        boolean userLikeYnCheck = userLikeRepository.existsByPlannerAndUser(planner,user);


        if(userSaveYnCheck){
            userSaveYn = "ok";
        }

        if(userLikeYnCheck){
            userLikeYn = "ok";
        }

        return UserLikeAndSaveYnResponse.builder()
                .userSaveYn(userSaveYn)
                .userLikeYn(userLikeYn)
                .build();


    }
}

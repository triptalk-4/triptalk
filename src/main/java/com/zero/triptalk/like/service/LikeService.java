package com.zero.triptalk.like.service;

import com.zero.triptalk.alert.entity.Alert;
import com.zero.triptalk.alert.repository.AlertRepository;
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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static com.zero.triptalk.exception.code.LikeErrorCode.*;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PlannerRepository plannerRepository;
    private final UserSaveRepository userSaveRepository;
    private final PlannerLikeRepository plannerLikeRepository;
    private final UserRepository userRepository;
    private final UserLikeRepository userLikeRepository;
    private final AlertRepository alertRepository;

    @Transactional
    public LikenOnePlusMinusResponse createLikeOrPlusPlanner(Long plannerId, String email) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new LikeException(NO_Planner_Detail_Board));

        // DetailPlannerLike 업데이트 또는 생성
        PlannerLike plannerLike = plannerLikeRepository.findByPlanner(planner);

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

        Alert alert = Alert.builder()
                .planner(planner)
                .user(user)
                .userCheckYn(false)
                .alertContent(user.getNickname()+" 님이"+planner.getTitle() +" 게시글에 좋아요를 누르셨습니다.")
                .nickname(planner.getUser().getNickname())
                .alertDt(LocalDateTime.now())
                .build();

        alertRepository.save(alert);

        if( plannerLike == null){
            plannerLike = PlannerLike.builder()
                    .planner(planner)
                    .likeCount(1L)
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
    public LikenOnePlusMinusResponse LikeOneMinus(Long plannerDetailId, String email) {
        Planner planner = plannerRepository.findById(plannerDetailId)
                .orElseThrow(() -> new LikeException(NO_Planner_Detail_Board));

        PlannerLike plannerLike = plannerLikeRepository.findByPlanner(planner);

        //플레너 디테일 좋아요취소
        long currentDetailLikeCount = plannerLike.getLikeCount();
        long newLikeCount = currentDetailLikeCount - 1;

        plannerLike.setLikeCount(newLikeCount);
        plannerLikeRepository.save(plannerLike);

        // 좋아요를 누른 접속자 유저 찾기
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

    public UserSaveAndCancelResponse userSavePlus(Long plannerId, String email) {
        // 게시글 찾기
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new LikeException(NO_Planner_Detail_Board));

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new LikeException(No_User_Search));

        UserSave userSave = userSaveRepository.findByPlannerAndUser(planner,user);

        if(userSave != null){
            throw new LikeException(LikeErrorCode.NO_SAVE_DUPLICATE_ERROR);
        }

        UserSave userSaveFin = UserSave.builder()
                              .planner(planner)
                              .user(user)
                              .saveDt(LocalDateTime.now())
                              .build();

        userSaveRepository.save(userSaveFin);

        Alert alertSaveFin = Alert.builder()
                .userCheckYn(false)
                .user(user)
                .planner(planner)
                .alertDt(LocalDateTime.now())
                .nickname(planner.getUser().getNickname())
                .alertContent(user.getNickname() + " 님이" + planner.getTitle() + " 게시물을 저장하였습니다.")
                .build();

        alertRepository.save(alertSaveFin);

        return UserSaveAndCancelResponse.builder()
                .ok("저장 추가가 완료되었습니다.")
                .build();
    }

    @Transactional
    public UserSaveAndCancelResponse userCancel(Long plannerId, String email) {

        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new LikeException(NO_Planner_Detail_Board));

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new LikeException(No_User_Search));

        UserSave userSaveDelete = userSaveRepository.findByPlannerAndUser(planner,user);

        if(userSaveDelete == null) {
            throw new LikeException(NO_SAVE_EXIST_ERROR);
        }

        userSaveRepository.delete(userSaveDelete);

        return UserSaveAndCancelResponse.builder()
                .ok("저장함 삭제가 완료되었습니다.")
                .build();
    }

    public UserLikeAndSaveYnResponse userCheckYn(Long plannerId, String email) {

        String userSaveYn = "no";
        String userLikeYn = "no";

        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new LikeException(NO_Planner_Detail_Board));

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new LikeException(No_User_Search));

        boolean userSaveYnCheck = userSaveRepository.existsByPlannerAndUser(planner,user);
        boolean userLikeYnCheck = userLikeRepository.existsByPlannerAndUser(planner,user);
        PlannerLike plannerLike = plannerLikeRepository.findByPlanner(planner);

         long likeCount = 0;

        if(plannerLike != null){
           likeCount = plannerLike.getLikeCount();
        }


        if(userSaveYnCheck){
            userSaveYn = "ok";
        }

        if(userLikeYnCheck){
            userLikeYn = "ok";
        }

        return UserLikeAndSaveYnResponse.builder()
                .userSaveYn(userSaveYn)
                .userLikeYn(userLikeYn)
                .likeCount(likeCount)
                .build();
    }

    public PlannerLike findByPlannerId(Long plannerId) {
        return plannerLikeRepository.findByPlanner_PlannerId(plannerId);
    }

    //존재하는지
    public boolean PlannerLikeExist(Planner planner){
        return plannerLikeRepository.existsByPlanner(planner);
    }

    public boolean UserLikeEntityExist(Planner planner){
        return userLikeRepository.existsByPlanner(planner);
    }

    public boolean UserSaveExist(Planner planner){
        return userSaveRepository.existsByPlanner(planner);
    }

    //삭제
    public void deletePlannerLike(Planner planner) {
        plannerLikeRepository.deleteAllByPlanner(planner);
    }

    public void deleteUserLikeEntity(Planner planner){
        userLikeRepository.deleteAllByPlanner(planner);
    }

    public void deleteUserSave(Planner planner){
        userSaveRepository.deleteAllByPlanner(planner);
    }

}

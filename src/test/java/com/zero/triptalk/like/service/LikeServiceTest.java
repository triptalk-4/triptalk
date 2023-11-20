package com.zero.triptalk.like.service;

import com.zero.triptalk.alert.entity.Alert;
import com.zero.triptalk.alert.repository.AlertRepository;
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
import com.zero.triptalk.planner.entity.PlannerDocument;
import com.zero.triptalk.planner.repository.PlannerRepository;
import com.zero.triptalk.planner.repository.PlannerSearchRepository;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {

    @InjectMocks
    private  LikeService likeService;

    @Mock
    private PlannerRepository plannerRepository;

    @Mock
    private PlannerLikeRepository plannerLikeRepository;

    @Mock
    private UserSaveRepository userSaveRepository;

    @Mock
    private UserLikeRepository userLikeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private PlannerSearchRepository plannerSearchRepository;

    @Test
    @DisplayName("좋아요 마이너스 할때 잘되는지  ")
    public void testLikeOneMinus() {
        // Arrange
        Long plannerDetailId = 1L;
        String email = "user@example.com";
        Planner planner = new Planner();
        PlannerLike plannerLike = new PlannerLike();
        plannerLike.setLikeCount(5L);
        plannerLike.setPlanner(planner);
        UserEntity user = new UserEntity();
        UserLikeEntity userLike = new UserLikeEntity();


        // 예외처리 관련 처리
        // plannerRepository.findById(plannerDetailId) -> 없으면 게시물 없음 에러 발생
        when(plannerRepository.findById(plannerDetailId)).thenReturn(Optional.of(planner));
        // plannerLikeRepository.findByPlanner(planner)).thenReturn(plannerLike)
        // -> plannerLike 에서 없다면 -> 좋아요를 등록하지 않았습니다. 에러 발생
        when(plannerLikeRepository.findByPlanner(planner)).thenReturn(plannerLike);
        // email -> 값이 없으면 -> 해당 유저를 찾을 수 없다는 에러 발생
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        // userLike -> 좋아요를 등록하지 않은 사람이 등록한다면 -> 좋아요를 찾을 수 없다는 에러 발생
        when(userLikeRepository.findByPlannerAndUser(planner, user)).thenReturn(Optional.of(userLike));



        // Act
        LikenOnePlusMinusResponse response = likeService.LikeOneMinus(plannerDetailId, email);

        // Assert
        assertNotNull(response);
        assertEquals("좋아요가 취소되었습니다", response.getOk());
        assertEquals(4L, response.getPlannerCount());

        verify(plannerLikeRepository, times(1)).save(plannerLike);
        verify(userLikeRepository, times(1)).delete(userLike);
    }


    @Test
    @DisplayName("유저가 해당 게시글 등록을 취소할 때")
    public void testUserCancel() {
        // Arrange
        Long plannerId = 1L;
        String email = "user@example.com";
        Planner planner = new Planner();
        planner.setPlannerId(plannerId);
        UserEntity user = new UserEntity();
        UserSave userSave = new UserSave();

        // plannerRepository.findById(plannerId)).thenReturn(Optional.of(planner)
        // 플래너 레포지토리에서 게시물 찾을때
        when(plannerRepository.findById(plannerId)).thenReturn(Optional.of(planner));
        // userRepository.findByEmail(email)).thenReturn(Optional.of(user)
        // 유저를 찾을 수 없다면 -> 유저를 찾을 수 없다는 에러 처리
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // userSaveRepository.findByPlannerAndUser(planner, user)).thenReturn(userSave)
        // user@example.com -> 없는 이메일이지만 userSave 를 생성자로 등록하면 null이 아니다.
        when(userSaveRepository.findByPlannerAndUser(planner, user)).thenReturn(userSave);

        // Act
        UserSaveAndCancelResponse response = likeService.userCancel(plannerId, email);

        // Assert
        assertNotNull(response);
        assertEquals("저장함 삭제가 완료되었습니다.", response.getOk());

        verify(userSaveRepository, times(1)).delete(userSave);
    }

    @Test
    @DisplayName("한 유저가 플래너 아이디가 1인 값을 비교 하였을 때 -> " +
            "좋아요 , 저장함 체크 유무 , 좋아요 값 보여주기 ")
    public void testUserCheckYn() {
        // Arrange
        Long plannerId = 1L;
        String email = "user@example.com";
        Planner planner = new Planner();
        planner.setPlannerId(plannerId);
        UserEntity user = new UserEntity();
        PlannerLike plannerLike = new PlannerLike();
        plannerLike.setLikeCount(5L);

        // plannerRepository.findById(plannerId)).thenReturn(Optional.of(planner)
        // 플래너 레포지토리에서 게시물 찾을때
        when(plannerRepository.findById(plannerId)).thenReturn(Optional.of(planner));
        // userRepository.findByEmail(email)).thenReturn(Optional.of(user)
        // 유저를 찾을 수 없다면 -> 유저를 찾을 수 없다는 에러 처리
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        // userSaveRepository.existsByPlannerAndUser(planner, user)).thenReturn

        // 있으면 response.getUserSaveYn -> ok 로 나옴 -> 없으면 false
        when(userSaveRepository.existsByPlannerAndUser(planner, user)).thenReturn(true);

        // 있으면 response.getUserLikeYn() -> ok 로 나옴 -> 없으면 false
        when(userLikeRepository.existsByPlannerAndUser(planner, user)).thenReturn(true);
        when(plannerLikeRepository.findByPlanner(planner)).thenReturn(plannerLike);

        // Act
        UserLikeAndSaveYnResponse response = likeService.userCheckYn(plannerId, email);

        // Assert
        assertNotNull(response);
        assertEquals("ok", response.getUserSaveYn());
        assertEquals("ok", response.getUserLikeYn());
        assertEquals(5L, response.getLikeCount());
    }
}

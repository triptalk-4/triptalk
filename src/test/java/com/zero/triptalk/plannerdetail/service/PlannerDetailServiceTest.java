package com.zero.triptalk.plannerdetail.service;

import com.zero.triptalk.exception.type.PlannerDetailException;
import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.place.entity.PlaceRequest;
import com.zero.triptalk.place.service.ImageService;
import com.zero.triptalk.place.service.PlaceService;
import com.zero.triptalk.plannerdetail.dto.PlannerDetailDto;
import com.zero.triptalk.plannerdetail.dto.PlannerDetailRequest;
import com.zero.triptalk.plannerdetail.entity.PlannerDetail;
import com.zero.triptalk.plannerdetail.repository.PlannerDetailRepository;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.enumType.UserTypeRole;
import com.zero.triptalk.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.zero.triptalk.exception.code.PlannerDetailErrorCode.NOT_FOUND_PLANNER_DETAIL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlannerDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlaceService placeService;

    @Mock
    private ImageService imageService;

    @Mock
    private PlannerDetailRepository plannerDetailRepository;

    @InjectMocks
    private PlannerDetailService plannerDetailService;

    @Test
    @DisplayName("상세 일정 만들기")
    void createPlannerDetail() throws IOException {

        //given
        Long planId = 1L;
        String email = "test@exam.com";
        List<MultipartFile> files = new ArrayList<>();

        UserEntity user = UserEntity.builder()
                .userId(1L)
                .UserType(UserTypeRole.USER)
                .nickname("11")
                .email("test@exam.com")
                .password("11")
                .build();

        Place place = Place.builder()
                .id(1L)
                .name("남산")
                .region("한국")
                .si("서울시")
                .gun("서울군")
                .gu("서울구")
                .address("남산상세")
                .latitude(10)
                .longitude(10)
                .build();

        PlannerDetailRequest request = PlannerDetailRequest.builder()
                .id(1L)
                .date(LocalDateTime.now())
                .description("테스트 요청입니다.")
                .placeInfo(new PlaceRequest("남산", "한국", "서울시", "서울군", "서울구", "남산상세", 10, 10))
                .build();

        List<String> images =
                List.of("https://triptalk-s3.s3.ap-northeast-2.amazonaws.com/8437334e-ee54-4138-b9ad-63f7f498429f.jpg");

//        List<Images> images = List.of(
//                        new Images("https://triptalk-s3.s3.ap-northeast-2.amazonaws.com/8437334e-ee54-4138-b9ad-63f7f498429f.jpg")
//                );


        ArgumentCaptor<PlannerDetail> captor = ArgumentCaptor.forClass(PlannerDetail.class);

        //when
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(placeService.savePlace(any())).thenReturn(place);
        when(imageService.uploadFiles(any())).thenReturn(images);
        System.out.println(images);
        boolean result = plannerDetailService.createPlannerDetail(planId, files, request, email);

        //then
        verify(plannerDetailRepository).save(captor.capture());
        PlannerDetail saved = captor.getValue();

        System.out.println(saved.getImages());
        Assertions.assertEquals(saved.getImages(), images);
        Assertions.assertTrue(result);
        Assertions.assertEquals(saved.getPlace(), place);

    }

    @Test
    @DisplayName("상세 일정 조회 - 해당 일정이 없는 경우")
    void getPlannerDetailNotFound() {
        //given
        Long plannerDetailId = 1L;
        //when
        when(plannerDetailRepository.findById(plannerDetailId)).thenReturn(Optional.empty());
        //then

        PlannerDetailException e = Assertions.assertThrows(PlannerDetailException.class, () ->
                plannerDetailService.getPlannerDetail(plannerDetailId));
        Assertions.assertEquals(e.getErrorCode(), NOT_FOUND_PLANNER_DETAIL);
    }

    @Test
    @DisplayName("상세 일정 조회 - 정상")
    void getPlannerDetail() {
        //given
        Long plannerDetailId = 1L;
        List<String> images = new ArrayList<>();
        Place place = new Place();
        PlannerDetail result = PlannerDetail.builder()
                .planId(1L)
                .userId(1L)
                .images(Arrays.asList("aa","bb"))
                .description("TT")
                .images(images)
                .place(place).build();

        //when
        when(plannerDetailRepository.findById(plannerDetailId)).thenReturn(Optional.ofNullable(result));
        //then
        PlannerDetailDto plannerDetail = plannerDetailService.getPlannerDetail(plannerDetailId);
        Assertions.assertDoesNotThrow(
                () -> plannerDetailService.getPlannerDetail(plannerDetailId)
        );
        assert result != null;
        Assertions.assertEquals(plannerDetail.getUserId(),result.getUserId());
    }
}
package com.zero.triptalk.application;

import com.zero.triptalk.exception.custom.PlannerDetailException;
import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.place.entity.PlaceRequest;
import com.zero.triptalk.image.service.ImageService;
import com.zero.triptalk.place.service.PlaceService;
import com.zero.triptalk.planner.dto.PlannerDetailListRequest;
import com.zero.triptalk.planner.dto.PlannerDetailRequest;
import com.zero.triptalk.planner.dto.PlannerRequest;
import com.zero.triptalk.planner.type.PlannerStatus;
import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.planner.entity.PlannerDetail;
import com.zero.triptalk.planner.repository.PlannerDetailRepository;
import com.zero.triptalk.planner.repository.PlannerRepository;
import com.zero.triptalk.planner.service.PlannerDetailService;
import com.zero.triptalk.planner.service.PlannerService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlannerApplicationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlaceService placeService;

    @Mock
    private ImageService imageService;

    @Mock
    private PlannerRepository plannerRepository;

    @Mock
    private PlannerDetailRepository plannerDetailRepository;

    @Mock
    private PlannerDetailService plannerDetailService;

    @Mock
    private PlannerService plannerService;

    @InjectMocks
    private PlannerApplication plannerApplication;

    @Test
    @DisplayName("상세 일정 한개 만들기")
    void createPlannerDetail() {

        //given
        Long plannerId = 1L;
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
                .placeId(1L)
                .name("남산")
                .region("한국")
                .si("서울시")
                .gun("서울군")
                .gu("서울구")
                .address("남산상세")
                .latitude(10)
                .longitude(10)
                .build();

        Planner planner = Planner.builder()
                .title("11")
                .build();

        PlannerDetailRequest request = PlannerDetailRequest.builder()
                .id(1L)
                .date(LocalDateTime.now())
                .description("테스트 요청입니다.")
                .placeInfo(new PlaceRequest("남산", "한국", "서울시", "서울군", "서울구","123", "남산상세", 10, 10))
                .build();

        List<String> images =
                List.of("https://triptalk-s3.s3.ap-northeast-2.amazonaws.com/8437334e-ee54-4138-b9ad-63f7f498429f.jpg");

//        List<Images> images = List.of(
//                        new Images("https://triptalk-s3.s3.ap-northeast-2.amazonaws.com/8437334e-ee54-4138-b9ad-63f7f498429f.jpg")
//                );

        ArgumentCaptor<PlannerDetail> captor = ArgumentCaptor.forClass(PlannerDetail.class);

        //when
        when(plannerDetailService.findByEmail(any())).thenReturn(user);
        when(plannerService.findById(plannerId)).thenReturn(planner);
        when(placeService.savePlace(any())).thenReturn(place);
        when(imageService.uploadFiles(any())).thenReturn(images);
        boolean result = plannerApplication.createPlannerDetail(plannerId, files, request, email);

        //then
        verify(plannerDetailService).savePlannerDetail(captor.capture());
        PlannerDetail saved = captor.getValue();

        Assertions.assertEquals(saved.getImages(), images);
        Assertions.assertTrue(result);
        Assertions.assertEquals(saved.getPlace(), place);
    }

    @Test
    @DisplayName("일정 생성")
    void createPlanner() {
        //given

        Long plannerId = 1L;
        String email = "test@exam.com";
        PlaceRequest placeRequest = PlaceRequest.builder().name("남산")
                .region("한국")
                .si("서울시")
                .gun("서울군")
                .gu("서울구")
                .address("남산상세")
                .latitude(10)
                .longitude(10)
                .build();
        List<String> images =
                List.of("https://triptalk-s3.s3.ap-northeast-2.amazonaws.com/8437334e-ee54-4138-b9ad-63f7f498429f.jpg");
        UserEntity user = UserEntity.builder()
                .userId(1L)
                .UserType(UserTypeRole.USER)
                .nickname("11")
                .email("test@exam.com")
                .password("11")
                .build();
        PlannerRequest plannerRequest = PlannerRequest.builder()
                .title("제목")
                .plannerStatus(PlannerStatus.PUBLIC)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .build();
        PlannerDetailListRequest plannerDetailListRequest = PlannerDetailListRequest.builder()
                .images(images)
                .placeInfo(placeRequest)
                .description("")
                .build();

        Planner planner = Planner.builder()
                .title("11")
                .build();
        Place place = placeRequest.toEntity();

        when(plannerDetailService.findByEmail(email)).thenReturn(user);
        when(plannerService.createPlanner(plannerRequest,user)).thenReturn(planner);
        when(placeService.savePlace(any())).thenReturn(place);


        //when
        boolean result = plannerApplication.createPlanner(plannerRequest,
                Collections.singletonList(plannerDetailListRequest), email);

        //then

        verify(plannerDetailService).savePlannerDetailList(any());
        Assertions.assertTrue(result);

    }

    @Test
    @DisplayName("상세 일정 삭제 - 성공")
    void deletePlannerDetail() {
        //given
        Long plannerDetailId = 1L;
        String email = "test@test.com";
        UserEntity user = UserEntity.builder()
                .userId(2L)
                .build();
        PlannerDetail plannerDetail = PlannerDetail.builder()
                .userId(2L)
                .build();
        List<String> images = new ArrayList<>();
        //when
        when(plannerDetailService.findByEmail(email)).thenReturn(user);
        when(plannerDetailService.findById(plannerDetailId)).thenReturn(plannerDetail);
        plannerApplication.deletePlannerDetail(plannerDetailId,email);
        //then

        imageService.deleteImages(images);
        verify(plannerDetailService).deletePlannerDetail(plannerDetailId);
    }

    @Test
    @DisplayName("상세 일정 삭제 - 사용자 불일치")
    void deletePlannerDetail_UNMATCHED() {
        //given
        Long plannerDetailId = 1L;
        String email = "test@test.com";
        UserEntity user = UserEntity.builder()
                .userId(2L)
                .build();
        PlannerDetail plannerDetail = PlannerDetail.builder()
                .userId(1L)
                .build();
        List<String> images = new ArrayList<>();
        //when
        when(plannerDetailService.findByEmail(email)).thenReturn(user);
        when(plannerDetailService.findById(plannerDetailId)).thenReturn(plannerDetail);
        //then
        imageService.deleteImages(images);
        Assertions.assertThrows(PlannerDetailException.class,
                () -> plannerApplication.deletePlannerDetail(plannerDetailId,email));
    }

}
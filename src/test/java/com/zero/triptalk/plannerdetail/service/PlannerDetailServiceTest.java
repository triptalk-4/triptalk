package com.zero.triptalk.plannerdetail.service;

import com.zero.triptalk.place.entity.Images;
import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.place.entity.PlaceRequest;
import com.zero.triptalk.place.service.ImageService;
import com.zero.triptalk.place.service.PlaceService;
import com.zero.triptalk.plannerdetail.dto.PlannerDetailRequest;
import com.zero.triptalk.plannerdetail.entity.PlannerDetail;
import com.zero.triptalk.plannerdetail.repository.PlannerDetailRepository;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.enumType.UserTypeRole;
import com.zero.triptalk.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
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
import java.util.List;
import java.util.Optional;

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

        List<Images> images = List.of(
                        new Images("https://triptalk-s3.s3.ap-northeast-2.amazonaws.com/8437334e-ee54-4138-b9ad-63f7f498429f.jpg")
                );
        System.out.println(images);


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
        Assertions.assertThat(saved.getImages()).isEqualTo(images);
        Assertions.assertThat(result).isTrue();
        Assertions.assertThat(saved.getPlace()).isEqualTo(place);
    }
}
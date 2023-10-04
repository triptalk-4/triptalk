package com.zero.triptalk.planner.service;

import com.zero.triptalk.exception.type.PlannerDetailException;
import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.place.service.ImageService;
import com.zero.triptalk.place.service.PlaceService;
import com.zero.triptalk.planner.dto.PlannerDetailRequest;
import com.zero.triptalk.planner.dto.PlannerDetailResponse;
import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.planner.entity.PlannerDetail;
import com.zero.triptalk.planner.repository.PlannerDetailRepository;
import com.zero.triptalk.planner.repository.PlannerRepository;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.zero.triptalk.exception.code.PlannerDetailErrorCode.NOT_FOUND_PLANNER_DETAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlannerDetailServiceTest {

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


    @InjectMocks
    private PlannerDetailService plannerDetailService;


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
                .planner(new Planner())
                .userId(1L)
                .images(Arrays.asList("aa", "bb"))
                .description("TT")
                .images(images)
                .place(place).build();

        //when
        when(plannerDetailRepository.findById(plannerDetailId)).thenReturn(Optional.ofNullable(result));
        //then
        PlannerDetailResponse response = plannerDetailService.getPlannerDetail(plannerDetailId);
        Assertions.assertDoesNotThrow(
                () -> plannerDetailService.getPlannerDetail(plannerDetailId)
        );
        assert result != null;
        Assertions.assertEquals(response.getUserId(), result.getUserId());
    }

    @Test
    @DisplayName("상세 일정 리스트 추가를 위한 이미지 리스트 업로드")
    void uploadImages() throws IOException {
        //given
        Path path = Paths.get("src/test/resources/cat.jpg");
        byte[] imageBytes = Files.readAllBytes(path);
        List<MultipartFile> images = List.of(
                new MockMultipartFile("files", "cat.jpg", "image/jpeg", imageBytes),
                new MockMultipartFile("files", "cat.jpg", "image/jpeg", imageBytes)
        );

        //when
        doReturn(Arrays.asList("http://127.0.0.1:8001/bucket-name/cat.jpg",
                "http://127.0.0.1:8001/bucket-name/cat.jpg"))
                .when(imageService)
                .uploadFiles(images);

        //then
        List<String> urlList = plannerDetailService.uploadImages(images);
        assertEquals(2, urlList.size());
        assertTrue(urlList.containsAll(Arrays.asList(
                "http://127.0.0.1:8001/bucket-name/cat.jpg",
                "http://127.0.0.1:8001/bucket-name/cat.jpg"
        )));
        verify(imageService).uploadFiles(images);
    }

    @Test
    @DisplayName("상세 일정 저장")
    void savePlannerDetail() {
        //given
        Planner planner = new Planner();
        PlannerDetailRequest plannerDetailRequest = new PlannerDetailRequest();
        UserEntity user = new UserEntity();
        Place place = new Place();
        List<String> images = new ArrayList<>();
        PlannerDetail plannerDetail = PlannerDetail.buildPlannerDetail(planner, plannerDetailRequest,
                user, place, images);

        //when
        //then
        plannerDetailService.savePlannerDetail(plannerDetail);
        verify(plannerDetailRepository, times(1)).save(any(PlannerDetail.class));

    }

    @Test
    @DisplayName("상세일정 리스트 저장")
    void savePlannerDetailList() {
        //given
        Planner planner = new Planner();
        PlannerDetailRequest plannerDetailRequest = new PlannerDetailRequest();
        UserEntity user = new UserEntity();
        Place place = new Place();
        List<String> images = new ArrayList<>();
        List<PlannerDetail> list = new ArrayList<>();
        PlannerDetail plannerDetail = PlannerDetail.buildPlannerDetail(planner, plannerDetailRequest,
                user, place, images);
        list.add(plannerDetail);

        //when
        //then
        plannerDetailService.savePlannerDetailList(list);
        verify(plannerDetailRepository, times(1)).saveAll(any(List.class));
    }

    @Test
    @DisplayName("상세 일정 ID를 통해 상세 일정 찾기")
    void findById() {
        //given
        Long plannerDetailId = 1L;
        PlannerDetail plannerDetail = PlannerDetail.builder()
                .description("설명")
                .build();
        //when
        doReturn(Optional.of(plannerDetail)).when(plannerDetailRepository).findById(plannerDetailId);
        PlannerDetail result = plannerDetailService.findById(plannerDetailId);
        //then
        Assertions.assertEquals("설명", result.getDescription());

    }

    @Test
    @DisplayName("상세 일정 ID를 통해 상세 일정 찾기- 해당 상세일정이 존재 하지 않는 경우")
    void findById_NOT_FOUND() {
        //given
        Long plannerDetailId = 1L;
        //when
        when(plannerDetailRepository.findById(plannerDetailId)).thenReturn(Optional.empty());
        //then
        Assertions.assertThrows(PlannerDetailException.class, () -> plannerDetailService.findById(plannerDetailId));
    }

    @Test
    @DisplayName("상세 일정 삭제")
    void deletePlannerDetail() {
        //given
        Long id = 1L;
        //when
        plannerDetailService.deletePlannerDetail(id);
        //then
        verify(plannerDetailRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("일정 id로 상세 일정 찾기 - 성공")
    void findByPlannerId() {
        //given
        Long plannerId = 1L;
        PlannerDetail plannerDetail = PlannerDetail.builder()
                .description("설명")
                .build();
        List<PlannerDetail> plannerDetails =Arrays.asList(plannerDetail,plannerDetail);
        //when
        when(plannerDetailRepository.findByPlanner_Id(plannerId)).thenReturn(plannerDetails);
        //then
        List<PlannerDetail> byPlannerId = plannerDetailService.findByPlannerId(plannerId);
        Assertions.assertEquals(byPlannerId.get(0),plannerDetail);
    }

    @Test
    @DisplayName("일정 id로 상세 일정 찾기 - 해당 상세 일정 없음")
    void findByPlannerId_NOT_FOUND() {
        //given
        Long plannerId = 1L;
        //when
        when(plannerDetailRepository.findByPlanner_Id(plannerId)).thenReturn(new ArrayList<>());
        //then
        Assertions.assertThrows(PlannerDetailException.class,
                () -> plannerDetailService.findByPlannerId(plannerId));
    }

}
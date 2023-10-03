package com.zero.triptalk.planner.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zero.triptalk.application.PlannerApplication;
import com.zero.triptalk.config.JwtService;
import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.place.entity.PlaceRequest;
import com.zero.triptalk.planner.dto.*;
import com.zero.triptalk.planner.service.PlannerDetailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlannerController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser(username = "testUser", roles = "USER")
class PlannerDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlannerDetailService plannerDetailService;

    @MockBean
    private PlannerApplication plannerApplication;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AmazonS3Client amazonS3Client;


    @Test
    @DisplayName("상세 일정 한개 만들기")
    void createPlannerDetail() throws Exception {

        //given
        Long plannerId = 1L;
        PlannerDetailRequest request = PlannerDetailRequest.builder()
                .date(LocalDateTime.now())
                .description("테스트 요청입니다.")
                .placeInfo(new PlaceRequest("남산", "한국", "서울시", "서울군", "서울구", "남산상세", 10, 10))
                .build();

        Path path = Paths.get("src/test/resources/cat.jpg");
        byte[] imageBytes = Files.readAllBytes(path);
        List<MultipartFile> files = List.of(
                new MockMultipartFile("files", "cat.jpg", "image/jpeg", imageBytes),
                new MockMultipartFile("files", "cat.jpg", "image/jpeg", imageBytes));

//        when(plannerDetailService.createPlannerDetail(1L, files, request, "1"))
//                .thenReturn(true);

        //when
        doReturn(true)
                .when(plannerApplication)
                .createPlannerDetail(plannerId, files, request, "postrel63@gmail");
        //then

        mockMvc.perform(multipart("/api/plans/{plannerId}/detail", plannerId)
                .file((MockMultipartFile) files.get(0))
                .file((MockMultipartFile) files.get(1))
                .file(new MockMultipartFile("request",
                        "",
                        "application/json",
                        objectMapper.writeValueAsString(request).getBytes()))
                .with(csrf())
        ).andExpect(status().isOk());
    }


    @Test
    @DisplayName("상세 일정 조회하기")
    void getPlannerDetail() throws Exception {
        //given
        Long PlannerDetailId = 1L;
        String description = "TT";
        Place place = new Place();
        List<String> images = new ArrayList<>();

        //when
        doReturn(PlannerDetailDto.builder()
                .createAt(LocalDateTime.now())
                .imagesUrl(images)
                .description(description)
                .place(place)
                .build()).when(plannerDetailService)
                .getPlannerDetail(PlannerDetailId);

        //then
        mockMvc.perform(get("/api/plans/{plannerDetailId}/detail", PlannerDetailId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(description));
    }

    @Test
    @DisplayName("일정 생성을 위한 사전 이미지 리스트 업로드")
    void uploadImages() throws Exception {
        //given
        Long plannerId = 1L;
        Path path = Paths.get("src/test/resources/cat.jpg");
        byte[] imageBytes = Files.readAllBytes(path);
        List<MultipartFile> images = List.of(
                new MockMultipartFile("files", "cat.jpg", "image/jpeg", imageBytes),
                new MockMultipartFile("files", "cat.jpg", "image/jpeg", imageBytes)
        );

        //when
        doReturn(Arrays.asList("http://127.0.0.1:8001/bucket-name/cat.jpg",
                "http://127.0.0.1:8001/bucket-name/cat.jpg"))
                .when(plannerDetailService)
                .uploadImages(images);

        //then
        MvcResult mvcResult = mockMvc.perform(multipart("/api/plans/{plannerId}/images", plannerId)
                        .file((MockMultipartFile) images.get(0))
                        .file((MockMultipartFile) images.get(1))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();


        String contentAsString = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> urlList = objectMapper.readValue(contentAsString, new TypeReference<List<String>>() {
        });

        assertEquals(2, urlList.size());
        assertTrue(urlList.containsAll(Arrays.asList(
                "http://127.0.0.1:8001/bucket-name/cat.jpg",
                "http://127.0.0.1:8001/bucket-name/cat.jpg"
        )));
    }

    @Test
    @DisplayName("일정 생성하기")
    void createPlanner() throws Exception {
        //given
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String email = "test@test.com";
        LocalDateTime date = LocalDateTime.of(2023, 10, 10, 10, 10);

        PlannerRequest plannerRequest = PlannerRequest.builder()
                .title("제목")
                .description("설명")
                .startDate(date)
                .endDate(date.plusDays(1))
                .plannerStatus(PlannerStatus.PUBLIC)
                .build();
        List<String> images = new ArrayList<>();
        images.add("urls");

        List<PlannerDetailListRequest> requests = new ArrayList<>();
        requests.add(PlannerDetailListRequest.builder()
                .date(date)
                .description("테스트 요청입니다.")
                .images(images)
                .placeInfo(new PlaceRequest("남산", "한국", "서울시", "서울군", "서울구", "남산상세", 10, 10))
                .build());
        CreatePlannerInfo info = CreatePlannerInfo.builder()
                .plannerDetailListRequests(requests)
                .plannerRequest(plannerRequest)
                .build();
        //when
        doReturn(true).when(plannerApplication).createPlanner(
                info.getPlannerRequest(), info.getPlannerDetailListRequests(), email
        );

        //then
        mockMvc.perform(post("/api/plans").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(info))
                        .with(csrf()))
                .andExpect(status().isOk());

    }


}
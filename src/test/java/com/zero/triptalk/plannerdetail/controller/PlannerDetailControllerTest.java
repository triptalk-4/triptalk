package com.zero.triptalk.plannerdetail.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.triptalk.config.JwtService;
import com.zero.triptalk.place.entity.Images;
import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.place.entity.PlaceRequest;
import com.zero.triptalk.plannerdetail.dto.PlannerDetailDto;
import com.zero.triptalk.plannerdetail.dto.PlannerDetailRequest;
import com.zero.triptalk.plannerdetail.service.PlannerDetailService;
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
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlannerDetailController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser(username = "testUser", roles = "USER")
class PlannerDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlannerDetailService plannerDetailService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @Test
    @DisplayName("상세 일정 만들기")
    void createPlannerDetail() throws Exception {

        //given
        Long planId = 1L;
        PlannerDetailRequest request = PlannerDetailRequest.builder()
                .id(1L)
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

        doReturn(true)
                .when(plannerDetailService)
                .createPlannerDetail(1L, files, request, "postrel63@gmail");
        //when
        //then

        mockMvc.perform(multipart("/api/plans/{planId}/detail", planId)
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
        List<Images> images = new ArrayList<>();

        //when
        doReturn(PlannerDetailDto.builder()
                .createAt(LocalDateTime.now())
                .images(images)
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

}
package com.zero.triptalk.plannerdetail.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.triptalk.config.JwtService;
import com.zero.triptalk.place.entity.PlaceRequest;
import com.zero.triptalk.plannerdetail.dto.PlannerDetailRequest;
import com.zero.triptalk.plannerdetail.service.PlannerDetailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
}
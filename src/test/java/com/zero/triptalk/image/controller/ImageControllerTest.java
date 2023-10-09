package com.zero.triptalk.image.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.triptalk.config.JwtService;
import com.zero.triptalk.image.domain.ImageRequest;
import com.zero.triptalk.image.service.ImageService;
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
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser(username = "testUser", roles = "USER")
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AmazonS3Client amazonS3Client;

    @Test
    void uploadImages() throws Exception {
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
        MvcResult mvcResult = mockMvc.perform(multipart("/api/images")
                        .file((MockMultipartFile) images.get(0))
                        .file((MockMultipartFile) images.get(1))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("사진 삭제")
    void deleteImage() throws Exception {
        ImageRequest request = ImageRequest.builder()
                .url("https://s3.aws.com/my-image.jpg")
                .build();
        String jsonRequest = objectMapper.writeValueAsString(request);

        doNothing().when(imageService).deleteFile(request.getUrl());
        mockMvc.perform(delete("/api/images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .with(csrf()))
                .andExpect(status().isNoContent());
        verify(imageService, times(1)).deleteFile(request.getUrl());
    }
}
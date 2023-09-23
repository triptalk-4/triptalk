package com.zero.triptalk.plannerdetail.service;

import com.zero.triptalk.place.repository.PlaceRepository;
import com.zero.triptalk.place.service.ImageService;
import com.zero.triptalk.place.service.PlaceService;
import com.zero.triptalk.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlannerDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlaceService placeService;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private PlannerDetailService plannerDetailService;

    @Test
    @DisplayName("상세 일정 만들기")
    void createPlannerDetail() {

        //given


        //when


        //then

    }
}
package com.zero.triptalk.planner.service;

import com.zero.triptalk.planner.dto.request.PlannerRequest;
import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.planner.repository.PlannerDetailSearchRepository;
import com.zero.triptalk.planner.repository.PlannerRepository;
import com.zero.triptalk.planner.repository.PlannerSearchRepository;
import com.zero.triptalk.planner.type.PlannerStatus;
import com.zero.triptalk.user.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlannerServiceTest {

    @Mock
    private PlannerRepository plannerRepository;

    @InjectMocks
    private PlannerService plannerService;

    @Mock
    private PlannerSearchRepository plannerSearchRepository;

    @Mock
    private PlannerDetailSearchRepository plannerDetailSearchRepository;


    @Test
    @DisplayName("일정 만들기")
    void createPlanner() {
        //given
        PlannerRequest request = PlannerRequest.builder()
                .title("제목")
                .description("설명")
                .plannerStatus(PlannerStatus.PUBLIC)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        Planner result = Planner.builder()
                .title("제목")
                .description("설명")
                .plannerStatus(PlannerStatus.PUBLIC)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();
        UserEntity user = new UserEntity();
        String thumbnail = "url";

        //when
        when(plannerRepository.save(any(Planner.class))).thenReturn(result);
        //then
        Planner planner = plannerService.createPlanner(request, user,thumbnail);
        Assertions.assertEquals(planner.getTitle(), result.getTitle());

    }

    @Test
    @DisplayName("일정 삭제하기")
    void deletePlanner() {
        //given
        Long plannerId = 1L;
        //when
        plannerService.deletePlanner(plannerId);
        //then
        verify(plannerRepository, times(1)).deleteById(plannerId);
    }
}
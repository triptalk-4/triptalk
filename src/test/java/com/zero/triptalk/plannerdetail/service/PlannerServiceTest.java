package com.zero.triptalk.plannerdetail.service;

import com.zero.triptalk.plannerdetail.dto.PlannerRequest;
import com.zero.triptalk.plannerdetail.dto.PlannerStatus;
import com.zero.triptalk.plannerdetail.entity.Planner;
import com.zero.triptalk.plannerdetail.repository.PlannerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlannerServiceTest {

    @Mock
    private PlannerRepository plannerRepository;

    @InjectMocks
    private PlannerService plannerService;

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

        //when
        when(plannerRepository.save(any(Planner.class))).thenReturn(result);
        //then
        Planner planner = plannerService.createPlanner(request);
        Assertions.assertEquals(planner.getTitle(), result.getTitle());

    }

}
package com.zero.triptalk.plannerdetail.dto;


import com.zero.triptalk.plannerdetail.entity.Planner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlannerRequest {

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private PlannerStatus plannerStatus;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Planner toEntity() {

        return Planner.builder()
                .title(title)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .planStatus(plannerStatus)
                .build();
    }

}

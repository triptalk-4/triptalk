package com.zero.triptalk.planner.dto;


import com.zero.triptalk.planner.entity.Planner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;

    public Planner toEntity() {

        return Planner.builder()
                .title(title)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .plannerStatus(plannerStatus)
                .build();
    }

}

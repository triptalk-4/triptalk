package com.zero.triptalk.planner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlannerListResponse {

    private Long plannerId;
    private String title;
    private String thumbnail;
    //나중에 Integer로 변경
    private Double likeCount;
    private Long views;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}

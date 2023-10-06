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
    private String imageUrl;
    private Integer plannerLike;
    private Integer views;
    private LocalDateTime createAt;

}

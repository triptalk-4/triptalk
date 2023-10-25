package com.zero.triptalk.planner.dto.response;

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
    private Long likeCount;
    private Long views;
    private LocalDateTime createAt; //일정 생성 날짜

}

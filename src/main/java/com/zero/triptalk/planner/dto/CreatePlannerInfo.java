package com.zero.triptalk.planner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlannerInfo {

    private List<PlannerDetailListRequest> plannerDetailListRequests;
    private PlannerRequest plannerRequest;
}

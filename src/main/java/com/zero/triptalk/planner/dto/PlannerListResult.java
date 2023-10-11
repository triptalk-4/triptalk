package com.zero.triptalk.planner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlannerListResult {

    Slice<PlannerListResponse> plannerListResponses;
    boolean hasNext;
}

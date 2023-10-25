package com.zero.triptalk.planner.dto.request;

import com.zero.triptalk.planner.dto.request.PlannerRequest;
import com.zero.triptalk.planner.dto.request.UpdatePlannerDetailListRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePlannerInfo {

    private List<UpdatePlannerDetailListRequest> updatePlannerDetailListRequests;
    private PlannerRequest plannerRequest;
    private List<String> deletedUrls;
}

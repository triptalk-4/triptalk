package com.zero.triptalk.planner.dto.response;

import com.zero.triptalk.planner.entity.PlannerDetail;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
public class PlannerDetailListResponse {
    private String image;
    private String location;

    @Builder
    public PlannerDetailListResponse(String image, String location) {
        this.image = image;
        this.location = location;
    }

    public static List<PlannerDetailListResponse> of(List<PlannerDetail> plannerDetails) {

        if (plannerDetails == null) {
            return Collections.emptyList();
        }

        List<PlannerDetailListResponse> list = new ArrayList<>();
        for (PlannerDetail x : plannerDetails) {
            list.add(PlannerDetailListResponse.builder()
                    .location(x.getPlace().getPlaceName())
                    .build());
        }

        return list;
    }
}

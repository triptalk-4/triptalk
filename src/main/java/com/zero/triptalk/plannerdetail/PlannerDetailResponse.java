package com.zero.triptalk.plannerdetail;

import lombok.Builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlannerDetailResponse {
    private String image;
    private String location;

    @Builder
    public PlannerDetailResponse(String image, String location) {
        this.image = image;
        this.location = location;
    }

    public static List<PlannerDetailResponse> of(List<PlannerDetail> plannerDetails) {

        if (plannerDetails == null) {
            return Collections.emptyList();
        }

        List<PlannerDetailResponse> list = new ArrayList<>();
        for (PlannerDetail x : plannerDetails) {
            list.add(PlannerDetailResponse.builder()
                                            .image(x.getImage())
                                            .location(x.getLocation())
                                            .build());
        }

        return list;
    }
}

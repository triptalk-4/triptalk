package com.zero.triptalk.plannerdetail.dto;

import com.zero.triptalk.plannerdetail.entity.PlannerDetail;
import lombok.Builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
                                            .image(x.getImage())
                                            .location(x.getPlace().getName())
                                            .build());
        }

        return list;
    }
}

package com.zero.triptalk.plannerdetail.dto;

import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.place.entity.PlaceRequest;
import com.zero.triptalk.plannerdetail.entity.PlannerDetail;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PlannerDetailListRequest {
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime date;
    private String description;
    private PlaceRequest placeInfo;
    private List<String> images;


    public PlannerDetail toEntity(Long planId, Long userId) {
        Place place = placeInfo.toEntity();
        return PlannerDetail.builder()
                .planId(planId)
                .userId(userId)
                .description(description)
                .images(images)
                .place(place)
                .build();
    }
}
package com.zero.triptalk.plannerdetail.dto;

import com.zero.triptalk.place.entity.Images;
import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.plannerdetail.entity.PlannerDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlannerDetailDto {

    private Long userId;
    private LocalDateTime createAt;
    private Place place;
    private String description;
    private List<Images> images;

    public static PlannerDetailDto ofEntity(PlannerDetail plannerDetail){
        return PlannerDetailDto.builder()
                .userId(plannerDetail.getUserId())
                .createAt(plannerDetail.getCreatedAt())
                .place(plannerDetail.getPlace())
                .description(plannerDetail.getDescription())
                .images(plannerDetail.getImages())
                .build();
    }
}

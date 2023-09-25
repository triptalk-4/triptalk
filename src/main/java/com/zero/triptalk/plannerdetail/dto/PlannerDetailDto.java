package com.zero.triptalk.plannerdetail.dto;

import com.zero.triptalk.place.entity.Images;
import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.place.entity.PlaceResponse;
import com.zero.triptalk.plannerdetail.entity.PlannerDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlannerDetailDto {

    private LocalDateTime createAt;
    private Place place;
    private String description;
    private List<String> imagesUrl;

    public static PlannerDetailDto ofEntity(PlannerDetail plannerDetail, List<String> imagesUrl){
        return PlannerDetailDto.builder()
                .createAt(plannerDetail.getCreatedAt())
                .place(plannerDetail.getPlace())
                .description(plannerDetail.getDescription())
                .imagesUrl(imagesUrl)
                .build();
    }
}

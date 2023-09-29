package com.zero.triptalk.plannerdetail.dto;

import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.place.entity.PlaceResponse;
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
public class PlannerDetailResponse {

    private Long userId;
    private LocalDateTime createAt;
    private PlaceResponse placeResponse;
    private String description;
    private List<String> imagesUrl;


    public static PlannerDetailResponse from(PlannerDetailDto dto) {
        Place place = dto.getPlace();

        return PlannerDetailResponse.builder()
                .userId(dto.getUserId())
                .createAt(dto.getCreateAt())
                .placeResponse(PlaceResponse.from(place))
                .description(dto.getDescription())
                .imagesUrl(dto.getImagesUrl())
                .build();
    }

}

package com.zero.triptalk.planner.dto;

import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.place.entity.PlaceResponse;
import com.zero.triptalk.planner.entity.PlannerDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlannerDetailResponse {

    private Long userId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime date;
    private PlaceResponse placeResponse;
    private String description;
    private List<String> imagesUrl;


    public static PlannerDetailResponse from(PlannerDetail dto) {
        Place place = dto.getPlace();

        return PlannerDetailResponse.builder()
                .userId(dto.getUserId())
                .date(dto.getDate())
                .placeResponse(PlaceResponse.from(place))
                .description(dto.getDescription())
                .imagesUrl(dto.getImages())
                .build();
    }

}

package com.zero.triptalk.planner.dto.request;

import com.zero.triptalk.place.entity.PlaceRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlannerDetailListRequest {
    private Long plannerDetailId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime date;
    private String description;
    private PlaceRequest placeInfo;
    private List<String> images;

}
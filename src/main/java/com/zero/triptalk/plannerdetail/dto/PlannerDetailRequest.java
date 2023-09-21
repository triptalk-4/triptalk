package com.zero.triptalk.plannerdetail.dto;

import com.zero.triptalk.place.entity.PlaceRequest;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class PlannerDetailRequest {
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @DateTimeFormat(pattern = "hh:mm")
    private LocalTime time;
    private String description;
    private String image;
    private PlaceRequest placeInfo;

//    private String location;
//    private List<ImageRequest> images;

}
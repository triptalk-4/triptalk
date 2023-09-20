package com.zero.triptalk.plannerdetail.dto;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class PlannerDetailRequest {
    private Long id;
    private Long userId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @DateTimeFormat(pattern = "hh:mm")
    private LocalTime time;

    private String image;
    private String location;
    private String description;

}
package com.zero.triptalk.place.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    private String placeName; //관광지 이름

    private String roadAddress; // 도로명

    private String addressName; // 지번주소

    private double latitude;

    private double longitude;

    private Long PlaceLike;

}

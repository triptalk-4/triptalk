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

    private String name; //관광지 이름

    private String si;

    private String gun;

    private String gu;

    private String postCode; // 우편 번호

    private String roadAddress; // 도로명 // 주소 서울시 종로구 세종로 광화문

    private double latitude;

    private double longitude;

    private Long PlaceLike;

}

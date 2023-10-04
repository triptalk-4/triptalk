package com.zero.triptalk.place.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceResponse {

    private String name;

    private String region;

    private String si;

    private String gun;

    private String gu;

    private String address;

    private double latitude;

    private double longitude;

    public static PlaceResponse from(Place place){
        return PlaceResponse.builder()
                .name(place.getName())
                .region(place.getRegion())
                .si(place.getSi())
                .gun(place.getGun())
                .gu(place.getGu())
                .address(place.getAddress())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .build();
    }

}

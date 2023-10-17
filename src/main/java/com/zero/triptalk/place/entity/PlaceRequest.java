package com.zero.triptalk.place.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceRequest {

    private String placeName;

    private String roadAddress;

    private String addressName;

    private double latitude;

    private double longitude;

    public Place toEntity(){
        return Place.builder()
                .placeName(placeName)
                .roadAddress(roadAddress)
                .addressName(addressName)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

}

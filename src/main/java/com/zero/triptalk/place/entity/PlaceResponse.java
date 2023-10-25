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

    private String placeName;

    private String roadAddress;

    private String addressName;

    private double latitude;

    private double longitude;

    public static PlaceResponse from(Place place){
        return PlaceResponse.builder()
                .placeName(place.getPlaceName())
                .roadAddress(place.getRoadAddress())
                .addressName(place.getAddressName())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .build();
    }

}

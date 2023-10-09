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

    private String name;

    private String si;

    private String gun;

    private String gu;

    private String postCode;

    private String roadAddress;

    private double latitude;

    private double longitude;

    public Place toEntity(){
        return Place.builder()
                .name(name)
                .si(si)
                .gun(gun)
                .gu(gu)
                .postCode(postCode)
                .roadAddress(roadAddress)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

}

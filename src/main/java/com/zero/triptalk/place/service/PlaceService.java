package com.zero.triptalk.place.service;

import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.place.entity.PlaceRequest;
import com.zero.triptalk.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    //장소 1개 추가
    public Place savePlace(PlaceRequest placeRequest) {

        //정보가 수정된 장소라면? -> 너무 많은 예외가 있으니 그냥 새로 등록.
        Place place = Place.builder()
                .name(placeRequest.getName())
                .si(placeRequest.getSi())
                .gun(placeRequest.getGun())
                .gu(placeRequest.getGu())
                .postCode(placeRequest.getPostCode())
                .roadAddress(placeRequest.getRoadAddress())
                .latitude(placeRequest.getLatitude())
                .longitude(placeRequest.getLongitude())
                .build();
        return placeRepository.save(place);
    }

    public Optional<Place> findByRoadAddress(String roadAddress) {
        return placeRepository.findByRoadAddress(roadAddress);
    }
}

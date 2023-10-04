package com.zero.triptalk.place.repository;

import com.zero.triptalk.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByAddressAndLatitudeAndLongitude(String address,double latitude, double longitude);

}

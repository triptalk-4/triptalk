package com.zero.triptalk.place.entity;


import com.zero.triptalk.plannerdetail.entity.PlannerDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String region;

    private String si;

    private String gun;

    private String gu;

    private String address;

    private double latitude;

    private double longitude;

    private Long PlaceLike;

}

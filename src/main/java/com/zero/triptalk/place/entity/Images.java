package com.zero.triptalk.place.entity;

import com.zero.triptalk.plannerdetail.entity.PlannerDetail;
import lombok.*;
import javax.persistence.*;

@Getter
@Entity
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Images {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

//    //사진이 속한 상세일정
//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "plannerDetail_id")
//    private PlannerDetail plannerDetail;
//
//    //사진이 속한 장소 -> 나중에 지도에 활용
//    @ManyToOne
//    @JoinColumn(name = "place")
//    private Place place;
//
//    public Images(String url, PlannerDetail plannerDetail,Place place){
//        this.url = url;
//        this.plannerDetail = plannerDetail;
//        this.place = place;
//    }

    public Images(String fileUrl) {
        this.url = fileUrl;
    }
}

package com.zero.triptalk.place.entity;

import com.zero.triptalk.plannerdetail.entity.PlannerDetail;
import lombok.*;

import javax.persistence.*;
import java.util.List;

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


    //plannerDetail id 저장
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plannerDetail_id")
    private PlannerDetail plannerDetail;

    public Images(String fileUrl,PlannerDetail plannerDetail) {
        this.url = fileUrl;
        this.plannerDetail = plannerDetail;
    }
}

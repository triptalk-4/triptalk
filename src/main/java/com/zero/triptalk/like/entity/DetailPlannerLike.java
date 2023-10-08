package com.zero.triptalk.like.entity;

import com.zero.triptalk.planner.entity.PlannerDetail;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(value = {AuditingEntityListener.class})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailPlannerLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long detailPlannerLikeId;

    @ManyToOne
    @JoinColumn(name = "planner_detail_Id")
    private PlannerDetail plannerDetail;

    private Double likeCount;

    private LocalDateTime likeDt;


}

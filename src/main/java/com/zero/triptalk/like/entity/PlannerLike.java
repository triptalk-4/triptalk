package com.zero.triptalk.like.entity;

import com.zero.triptalk.planner.entity.Planner;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlannerLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long plannerLikeId;

    @ManyToOne
    @JoinColumn(name = "planner_id")
    private Planner planner;

    private Long likeCount;

    private LocalDateTime likeDt;
}

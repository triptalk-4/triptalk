package com.zero.triptalk.like.repository;

import com.zero.triptalk.like.entity.PlannerLike;
import com.zero.triptalk.planner.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PlannerLikeRepository extends JpaRepository<PlannerLike, Long> {

    PlannerLike findByPlanner(Planner planner);
    List<PlannerLike> findAllByLikeDtBetween(LocalDateTime from, LocalDateTime to);
}

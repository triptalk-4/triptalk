package com.zero.triptalk.like.repository;

import com.zero.triptalk.like.entity.PlannerLike;
import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.planner.entity.PlannerDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlannerLikeRepository extends JpaRepository<PlannerLike, Long> {

    PlannerLike findByPlanner(Planner planner);
}

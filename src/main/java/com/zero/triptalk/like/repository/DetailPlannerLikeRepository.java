package com.zero.triptalk.like.repository;

import com.zero.triptalk.like.entity.DetailPlannerLike;
import com.zero.triptalk.planner.entity.PlannerDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailPlannerLikeRepository extends JpaRepository<DetailPlannerLike, Long> {


    DetailPlannerLike findByPlannerDetail(PlannerDetail plannerDetail);
}

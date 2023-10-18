package com.zero.triptalk.like.repository;

import com.zero.triptalk.like.entity.PlannerLike;
import com.zero.triptalk.planner.entity.Planner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PlannerLikeRepository extends JpaRepository<PlannerLike, Long> {

    PlannerLike findByPlanner(Planner planner);

    PlannerLike findByPlanner_PlannerId(Long plannerId);

    @Query("SELECT pl, COUNT(pl) as likeCount FROM PlannerLike pl WHERE pl.planner = :planner GROUP BY pl")
    Page<Object[]> findLikesCountByPlanner(@Param("planner") Planner planner, Pageable pageable);

    Long countByPlanner(Planner planner);
  
    List<PlannerLike> findAllByLikeDtBetween(LocalDateTime from, LocalDateTime to);

}

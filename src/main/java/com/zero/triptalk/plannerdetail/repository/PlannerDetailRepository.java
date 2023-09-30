package com.zero.triptalk.plannerdetail.repository;

import com.zero.triptalk.plannerdetail.entity.Planner;
import com.zero.triptalk.plannerdetail.entity.PlannerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlannerDetailRepository extends JpaRepository<PlannerDetail, Long> {

    List<PlannerDetail> findByPlanner(Planner planner);
}

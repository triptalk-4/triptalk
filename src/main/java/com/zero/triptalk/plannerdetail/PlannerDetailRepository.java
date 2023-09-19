package com.zero.triptalk.plannerdetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannerDetailRepository extends JpaRepository<PlannerDetail, Long> {
}

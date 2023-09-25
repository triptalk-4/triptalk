package com.zero.triptalk.plannerdetail.repository;

import com.zero.triptalk.plannerdetail.dto.PlannerDetailResponse;
import com.zero.triptalk.plannerdetail.entity.PlannerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannerDetailRepository extends JpaRepository<PlannerDetail, Long> {


}

package com.zero.triptalk.reply.repository;

import com.zero.triptalk.planner.entity.PlannerDetail;
import com.zero.triptalk.reply.entity.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {
    List<ReplyEntity> findByPlannerDetail(PlannerDetail plannerDetail);
}

package com.zero.triptalk.plannerdetail.repository;

import com.zero.triptalk.plannerdetail.entity.Planner;
import com.zero.triptalk.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerRepository extends JpaRepository<Planner, Long> {
    List<Planner> findByUser(UserEntity user);
}

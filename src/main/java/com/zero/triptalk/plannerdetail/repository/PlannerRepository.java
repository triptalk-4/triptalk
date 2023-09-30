package com.zero.triptalk.plannerdetail.repository;

import com.zero.triptalk.plannerdetail.entity.Plan;
import com.zero.triptalk.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerRepository extends JpaRepository<Plan,Long> {
    List<Plan> findByUser(UserEntity user);
}

package com.zero.triptalk.like.repository;

import com.zero.triptalk.like.entity.UserLikeEntity;
import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLikeRepository extends JpaRepository<UserLikeEntity, Long> {

    boolean existsByPlannerAndUser(Planner planner, UserEntity userEntity);

    Optional<Object> findByPlannerAndUser(Planner planner, UserEntity user);

    boolean existsByPlanner(Planner planner);

    void deleteAllByPlanner(Planner planner);
}

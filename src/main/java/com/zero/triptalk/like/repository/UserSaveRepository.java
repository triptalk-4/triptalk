package com.zero.triptalk.like.repository;

import com.zero.triptalk.like.entity.UserSave;
import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserSaveRepository extends JpaRepository<UserSave, Long> {
    UserSave findByPlannerAndUser(Planner planner, UserEntity user);

    Boolean existsByPlannerAndUser(Planner planner, UserEntity user);

    @Query("SELECT us.planner, COALESCE(pl.likeCount, 0) AS likeCount\n" +
            "FROM UserSave us\n" +
            "LEFT JOIN PlannerLike pl ON us.planner = pl.planner\n" +
            "WHERE us.user = :user\n" +
            "GROUP BY pl.planner\n" +
            "ORDER BY us.saveDt DESC")
    Page<Object[]> findPlannersLikedByUserWithLikeCount(@Param("user") UserEntity user, Pageable pageable);

    boolean existsByPlanner(Planner planner);

    void deleteAllByPlanner(Planner planner);
}

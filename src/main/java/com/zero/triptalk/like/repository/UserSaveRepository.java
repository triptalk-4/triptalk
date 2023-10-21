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

    @Query("SELECT pl.planner, pl.likeCount as likeCount\n" +
            "FROM UserSave ple\n" +
            "JOIN PlannerLike pl ON ple.planner = pl.planner\n" +
            "WHERE ple.user = :user\n" +
            "GROUP BY pl.planner\n" +
            "ORDER BY ple.saveDt DESC")
    Page<Object[]> findPlannersLikedByUserWithLikeCount(@Param("user") UserEntity user, Pageable pageable);

    boolean existsByPlanner(Planner planner);

    void deleteAllByPlanner(Planner planner);
}

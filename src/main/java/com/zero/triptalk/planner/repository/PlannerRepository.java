package com.zero.triptalk.planner.repository;

import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlannerRepository extends JpaRepository<Planner, Long> {

    @Modifying
    @Query("update Planner p set p.views = :view where p.plannerId = :plannerId")
    void updateViews(@Param("view")Long view, @Param("plannerId") Long plannerId);

    @Query("SELECT p, COALESCE(pll.likeCount, 0) as likeCount\n" +
            "FROM Planner p\n" +
            "LEFT JOIN PlannerLike pll ON p.plannerId = pll.planner.plannerId\n" +
            "WHERE p.user = :user\n"  +
            "ORDER BY p.createdAt DESC")
    Page<Object[]> findPlannersWithLikeCount(@Param("user") UserEntity user, Pageable pageable);
}

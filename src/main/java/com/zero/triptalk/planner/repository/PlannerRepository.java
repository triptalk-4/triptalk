package com.zero.triptalk.planner.repository;

import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlannerRepository extends JpaRepository<Planner, Long> {
    List<Planner> findByUser(UserEntity user);
    Page<Planner> findByUser(UserEntity user, Pageable pageable);


    @Query("SELECT p, COALESCE(pll.likeCount, 0) as likeCount\n" +
            "FROM Planner p\n" +
            "LEFT JOIN PlannerLike pll ON p.id = pll.planner.id\n" +
            "WHERE p.user = :user\n"  +
            "ORDER BY p.createAt DESC")
    Page<Object[]> findPlannersWithLikeCount(@Param("user") UserEntity user, Pageable pageable);
}

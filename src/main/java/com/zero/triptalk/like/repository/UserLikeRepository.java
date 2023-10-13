package com.zero.triptalk.like.repository;

import com.zero.triptalk.like.entity.UserLikeEntity;
import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.planner.entity.PlannerDetail;
import com.zero.triptalk.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserLikeRepository extends JpaRepository<UserLikeEntity, Long> {

    boolean existsByPlannerDetailAndUser(PlannerDetail plannerDetail, UserEntity userEntity);

    Optional<Object> findByPlannerDetailAndUser(PlannerDetail plannerDetail, UserEntity user);

    @Query("SELECT pl, COUNT(pl) as likeCount FROM PlannerLike pl WHERE pl.planner = :planner GROUP BY pl")
    List<Object[]> findLikesCountByPlanner(@Param("planner") Planner planner);

    // 다음과 같이 유저가 좋아요 한 플래너만을 가져오는 메서드를 정의합니다.
    @Query("SELECT pl.planner, COUNT(pl) as likeCount " +
            "FROM PlannerLike pl " +
            "WHERE pl.planner.user = :user " +
            "GROUP BY pl.planner")
    Page<Object[]> findPlannersLikedByUserWithLikeCount(@Param("user") UserEntity user, Pageable pageable);


}

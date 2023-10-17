package com.zero.triptalk.planner.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.zero.triptalk.like.entity.QDetailPlannerLike.detailPlannerLike;
import static com.zero.triptalk.planner.entity.QPlannerDetail.plannerDetail;

@Repository
@RequiredArgsConstructor
public class CustomPlannerDetailRepository {

    private final JPAQueryFactory queryFactory;

    public List<Tuple> findAllByDateAndViewsAndLikesUpdateDt(
                                                    LocalDateTime from, LocalDateTime to) {


        return queryFactory.select(plannerDetail,
                            detailPlannerLike.likeDt, detailPlannerLike.likeCount)
                        .from(plannerDetail)
                        .join(detailPlannerLike)
                        .on(plannerDetail.plannerDetailId.eq(detailPlannerLike.plannerDetail.plannerDetailId))
                        .where(plannerDetail.modifiedAt.between(from, to)
                                .or(detailPlannerLike.likeDt.between(from, to)))
                        .groupBy(plannerDetail.plannerDetailId)
                        .fetch();
    }

}

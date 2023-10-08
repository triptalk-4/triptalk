package com.zero.triptalk.planner.repository;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.triptalk.like.entity.QPlannerLike;
import com.zero.triptalk.planner.dto.PlannerListResponse;
import com.zero.triptalk.planner.entity.QPlanner;
import com.zero.triptalk.planner.type.SortType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomPlannerRepository {

    private final JPAQueryFactory queryFactory;
    private final QPlanner qPlanner = QPlanner.planner;
    private final QPlannerLike qPlannerLike = QPlannerLike.plannerLike;

    public CustomPlannerRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<PlannerListResponse> PlannerList(Long lastId, int limit, SortType sortType) {

        final ConstructorExpression<PlannerListResponse> plannerListResponse =
                Projections.constructor(PlannerListResponse.class,
                        QPlanner.planner.plannerId,
                        QPlanner.planner.title,
                        QPlanner.planner.thumbnail,
                        QPlannerLike.plannerLike.likeCount,
                        QPlanner.planner.views,
                        QPlanner.planner.startDate,
                        QPlanner.planner.endDate
                );


        final List<PlannerListResponse> result = queryFactory
                .query()
                .select(plannerListResponse)
                .from(qPlanner)
                .leftJoin(qPlannerLike)
                .on(qPlanner.plannerId.eq(qPlannerLike.plannerLikeId))
                .where(qPlanner.plannerId.lt(lastId))
                .groupBy(qPlanner.plannerId)
                .orderBy(ordering(sortType))
                .limit(limit + 1)
                .fetch();

        if (result.size() > limit) {
            result.remove(limit);
        }

        return result;

    }

    private OrderSpecifier<?> ordering(final SortType sortType) {
        OrderSpecifier<?> orderSpecifier;
        switch (sortType) {
            case LIKES:
                orderSpecifier = qPlannerLike.likeCount.desc();
                break;
            case RECENT:
                orderSpecifier = qPlanner.createAt.desc();
                break;
            default:
                orderSpecifier = qPlanner.plannerId.desc();
        }
        return orderSpecifier;
    }
}

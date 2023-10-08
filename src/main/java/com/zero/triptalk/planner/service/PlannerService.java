package com.zero.triptalk.planner.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.triptalk.exception.code.PlannerErrorCode;
import com.zero.triptalk.exception.custom.PlannerException;
import com.zero.triptalk.planner.dto.PlannerListResponse;
import com.zero.triptalk.planner.dto.PlannerRequest;
import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.planner.repository.CustomPlannerRepository;
import com.zero.triptalk.planner.repository.PlannerRepository;
import com.zero.triptalk.planner.type.SortType;
import com.zero.triptalk.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final PlannerRepository plannerRepository;
    private final JPAQueryFactory queryFactory;
    private final CustomPlannerRepository customPlannerRepository;


    public Planner createPlanner(PlannerRequest request, UserEntity user) {
        return plannerRepository.save(request.toEntity(user));
    }

    public Planner findById(Long plannerId) {
        return plannerRepository.findById(plannerId).orElseThrow(
                () -> new PlannerException(PlannerErrorCode.NOT_FOUND_PLANNER));
    }

    public void deletePlanner(Long plannerId) {
        plannerRepository.deleteById(plannerId);
    }

    public List<PlannerListResponse> getPlanners(Long lastId, int limit, SortType sortType) {
        return customPlannerRepository.PlannerList(lastId, limit, sortType);

    }
}

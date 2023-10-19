package com.zero.triptalk.planner.service;

import com.zero.triptalk.exception.code.PlannerErrorCode;
import com.zero.triptalk.exception.custom.PlannerException;
import com.zero.triptalk.planner.dto.PlannerListResult;
import com.zero.triptalk.planner.dto.PlannerRequest;
import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.planner.repository.CustomPlannerRepository;
import com.zero.triptalk.planner.repository.PlannerDetailSearchRepository;
import com.zero.triptalk.planner.repository.PlannerRepository;
import com.zero.triptalk.planner.repository.PlannerSearchRepository;
import com.zero.triptalk.planner.type.SortType;
import com.zero.triptalk.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final PlannerRepository plannerRepository;
    private final CustomPlannerRepository customPlannerRepository;
    private final PlannerSearchRepository plannerSearchRepository;
    private final PlannerDetailSearchRepository plannerDetailSearchRepository;


    public Planner createPlanner(PlannerRequest request, UserEntity user, String thumbnail) {
        return plannerRepository.save(request.toEntity(user, thumbnail));
    }

    public Planner findById(Long plannerId) {
        return plannerRepository.findById(plannerId).orElseThrow(
                () -> new PlannerException(PlannerErrorCode.NOT_FOUND_PLANNER));
    }

    public void deletePlanner(Long plannerId) {
        plannerRepository.deleteById(plannerId);
        plannerSearchRepository.deleteById(plannerId);
        plannerDetailSearchRepository.deleteAllByPlannerId(plannerId);
    }

    public PlannerListResult getPlanners(Pageable pageable, SortType sortType) {
        return customPlannerRepository.PlannerList(pageable, sortType);

    }
}

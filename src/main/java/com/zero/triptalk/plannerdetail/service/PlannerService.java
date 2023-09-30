package com.zero.triptalk.plannerdetail.service;

import com.zero.triptalk.plannerdetail.dto.PlannerRequest;
import com.zero.triptalk.plannerdetail.entity.Planner;
import com.zero.triptalk.plannerdetail.repository.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final PlannerRepository plannerRepository;

    public Planner createPlanner(PlannerRequest request) {
       return plannerRepository.save(request.toEntity());
    }

}

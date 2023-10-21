package com.zero.triptalk.planner.service;

import com.zero.triptalk.component.RedisUtil;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlannerService {

    private final PlannerRepository plannerRepository;
    private final CustomPlannerRepository customPlannerRepository;
    private final PlannerSearchRepository plannerSearchRepository;
    private final PlannerDetailSearchRepository plannerDetailSearchRepository;
    private final StringRedisTemplate stringRedisTemplate;


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


    public void increaseViews(Planner planner){
        String redisKey = "planner:views:"+planner.getPlannerId();
        Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(redisKey, String.valueOf(planner.getViews() + 1), 4L, TimeUnit.MINUTES);
        if (Boolean.FALSE.equals(result)){
        stringRedisTemplate.opsForValue().increment(redisKey);
        stringRedisTemplate.expire(redisKey,4L,TimeUnit.MINUTES);
        }


    }
}

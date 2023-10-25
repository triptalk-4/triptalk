package com.zero.triptalk.planner.service;

import com.zero.triptalk.exception.code.PlannerErrorCode;
import com.zero.triptalk.exception.custom.PlannerException;
import com.zero.triptalk.planner.dto.PlannerListResult;
import com.zero.triptalk.planner.dto.PlannerRequest;
import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.planner.entity.PlannerDocument;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        Planner planner = plannerRepository.save(request.toEntity(user, thumbnail));
        plannerSearchRepository.save(PlannerDocument.ofEntity(planner));
        return planner;
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

    private void getAdd(Long plannerId, String key) {
        stringRedisTemplate.opsForSet().add(key, String.valueOf(plannerId));
    }

    public void increaseViews(Long plannerId) {
        plannerRepository.increaseViews(plannerId);
    }

    public Boolean checkDuplication(Long plannerId, Long loginUserId) {
        String key = "user:" + loginUserId;
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(key))) {
            getAdd(plannerId, key);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime midnight = LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.MIDNIGHT);
            long secondsUntilMidnight = Duration.between(now, midnight).toSeconds();
            stringRedisTemplate.expire(key, secondsUntilMidnight, TimeUnit.SECONDS);
            return false;
        }
        if (Boolean.FALSE.equals(stringRedisTemplate.opsForSet().isMember(key, String.valueOf(plannerId)))) {
            getAdd(plannerId, key);
            return false;
        }
        return true;
    }

    /**
     * userId를 key 로 하여 방문한 페이지를 set 으로 저장 , 조회수도 캐시로 저장
     */
    public void increaseViewsUser(Long views, Long plannerId, Long loginUserId) {
        String key = "user:" + loginUserId;
        String totalView = "planner:views:" + plannerId;

        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(key))) {
            getAdd(plannerId, key);
            stringRedisTemplate.expire(key, 24, TimeUnit.HOURS);
            stringRedisTemplate.opsForValue().set(totalView, String.valueOf(views + 1), 4L, TimeUnit.MINUTES);
            //user set 이 있고 조회된 적이 없으면
        } else if (Boolean.FALSE.equals(stringRedisTemplate.opsForSet().isMember(key, String.valueOf(plannerId)))) {
            getAdd(plannerId, key);
            if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(totalView))) {
                stringRedisTemplate.opsForValue().set(totalView, String.valueOf(views));
            }
            stringRedisTemplate.opsForValue().increment(totalView);
            stringRedisTemplate.expire(totalView, 4L, TimeUnit.MINUTES);
        }
    }
}

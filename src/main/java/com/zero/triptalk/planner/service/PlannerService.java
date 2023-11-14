package com.zero.triptalk.planner.service;

import com.zero.triptalk.component.RedisUtil;
import com.zero.triptalk.exception.code.PlannerErrorCode;
import com.zero.triptalk.exception.custom.PlannerException;
import com.zero.triptalk.planner.dto.request.PlannerRequest;
import com.zero.triptalk.planner.dto.response.PlannerListResult;
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
import org.redisson.api.RKeys;
import org.redisson.api.RLock;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final RedisUtil redisUtil;
    private final RedissonClient redissonClient;


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
    }

    public PlannerListResult getPlanners(Pageable pageable, SortType sortType) {
        return customPlannerRepository.PlannerList(pageable, sortType);
    }

    public void increaseViews(Long plannerId) {
        plannerRepository.increaseViews(plannerId);
    }

    public Boolean checkDuplication(Long plannerId, Long loginUserId) {
        String userKey = "user:" + loginUserId;
        RKeys keys = redissonClient.getKeys();

        RSet<String> userSet = redissonClient.getSet(userKey);
        RLock lock = redissonClient.getLock(userKey + ":lock");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.MIDNIGHT);
        long secondsUntilMidnight = Duration.between(now, midnight).toSeconds();

        lock.lock();
        try {
            if (userSet.isEmpty()) {
                userSet.add(String.valueOf(plannerId));
                keys.expire(userKey, secondsUntilMidnight, TimeUnit.SECONDS);
                return true;
            }
            if (userSet.contains(String.valueOf(plannerId))) {
                return false;
            }
            userSet.add(String.valueOf(plannerId));
        } finally {
            lock.unlock();
        }
        return true;
    }

    /**
     * userId를 key 로 하여 방문한 페이지를 set 으로 저장 , 조회수도 캐시로 저장
     */
    @Transactional
    public void increaseViewsUser(Long views, Long plannerId, Long loginUserId) {
        String userKey = "user:" + loginUserId;
        String plannerKey = "planner:views:" + plannerId;

        if (!redisUtil.hasKey(userKey)) {
            redisUtil.addUserSet(userKey, plannerId);
            redisUtil.setExpireMidnight(userKey);
            if (redisUtil.getData(plannerKey) == null) {
                redisUtil.setData(plannerKey, String.valueOf(views + 1));
            } else {
                redisUtil.increaseViews(plannerKey);
            }
        } else if (!redisUtil.isMember(userKey, String.valueOf(plannerId))) {
            redisUtil.addUserSet(userKey, plannerId);
            redisUtil.increaseViews(plannerKey);
        }
    }
}
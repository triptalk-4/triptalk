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


    /**
     * plannerId을 key 로 하여 방문한 사용자들을 set 으로 저장
     *
     */

    public void increaseViews(Planner planner, Long userId) {
        String user = String.valueOf(userId);
        String key = "plannerId:" + planner.getPlannerId();
        String totalViewsKey = "planner:views:" + planner.getPlannerId();
        // set에 key가 존재하지 않으면(최근 24시간 내에 조회된 적이 없으면)
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(key))) {
            stringRedisTemplate.opsForSet().add(key, user);
            stringRedisTemplate.expire(key, 24, TimeUnit.HOURS);
            stringRedisTemplate.opsForValue().set(totalViewsKey, String.valueOf(planner.getViews() + 1), 4L, TimeUnit.MINUTES);
            //레디스에 이미 올라가있음, 거기에 set에 아이디가 없으면 아이디 추가, 조회수 1 증가, 만료시간 설정
        } else if (Boolean.FALSE.equals(stringRedisTemplate.opsForSet().isMember(key, user))) {
            stringRedisTemplate.opsForSet().add(key, user);
            if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(totalViewsKey))) {
                stringRedisTemplate.opsForValue().set(totalViewsKey, String.valueOf(planner.getViews()));
            }
            stringRedisTemplate.opsForValue().increment(totalViewsKey);
            stringRedisTemplate.expire(totalViewsKey, 4L, TimeUnit.MINUTES);
        }
    }

    /**
     * userId를 key 로 하여 방문한 페이지를 set 으로 저장
     *
     */
    public void increaseViewsUser(Long views, Long plannerId, Long loginUserId) {
        String key = "user:" + loginUserId;
        String totalView = "planner:views:" + plannerId;

        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(key))) {
            stringRedisTemplate.opsForSet().add(key, String.valueOf(plannerId));
            stringRedisTemplate.expire(key, 24, TimeUnit.HOURS);
            stringRedisTemplate.opsForValue().set(totalView, String.valueOf(views + 1), 4L, TimeUnit.MINUTES);
            //user set 이 있고 조회된 적이 없으면
        } else if (Boolean.FALSE.equals(stringRedisTemplate.opsForSet().isMember(key, String.valueOf(plannerId)))) {
            stringRedisTemplate.opsForSet().add(key, String.valueOf(plannerId));
            if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(totalView))) {
                stringRedisTemplate.opsForValue().set(totalView, String.valueOf(views));
            }
            stringRedisTemplate.opsForValue().increment(totalView);
            stringRedisTemplate.expire(totalView, 4L, TimeUnit.MINUTES);
        }
    }
}

package com.zero.triptalk.component;

import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.planner.repository.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ViewSynchronizer {

    private final StringRedisTemplate stringRedisTemplate;
    private final PlannerRepository plannerRepository;
    private final EntityManager entityManager;


    /**
     * look aside + write back
     * 조회수 동기화
     *
     *
     **/
    @Transactional
//    @Scheduled(cron = "0 0/3 * * * *")
    public void SynchronizerViews() {
        Set<String> keys = stringRedisTemplate.keys("planner:views:*");
        assert keys != null;
        for (String key : keys) {
            Long plannerId = Long.parseLong(key.split(":")[2]);
            String view = stringRedisTemplate.opsForValue().get(key);
            Optional<Planner> optionalPlanner = plannerRepository.findById(plannerId);
            if (optionalPlanner.isEmpty()) {
                continue;
            } else {
                Planner planner = optionalPlanner.get();
                assert view != null;
                planner.setViews(Long.valueOf(view));
                plannerRepository.save(planner);

                entityManager.flush();
                entityManager.clear();
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 0/3 * * * *")
    public void SynchronizerViewsSQL() {
        Set<String> keys = stringRedisTemplate.keys("planner:views:*");
        assert keys != null;
        for (String key : keys) {
            Long plannerId = Long.parseLong(key.split(":")[2]);
            String view = stringRedisTemplate.opsForValue().get(key);
            assert view != null;
            plannerRepository.updateViews(Long.valueOf(view), plannerId);
        }
    }
}

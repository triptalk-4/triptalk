package com.zero.triptalk.component;

import com.zero.triptalk.planner.repository.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ViewsSynchronizer {

    private final StringRedisTemplate stringRedisTemplate;
    private final PlannerRepository plannerRepository;

    /**
     * look aside + write back
     * 조회수 동기화
     **/
    @Transactional
//    @Scheduled(cron = "0 0/3 * * * *")
    public void updateDBFromRedisBySQL() {

        ScanOptions options = ScanOptions.scanOptions()
                .match("planner:views:*")
                .count(10)
                .build();
        Cursor<byte[]> cursor = stringRedisTemplate.executeWithStickyConnection(
                connection -> connection.scan(options)
        );
        if (cursor != null) {
            while (cursor.hasNext()) {
                String key = new String(cursor.next());
                Long plannerId = Long.parseLong(key.split(":")[2]);
                String view = stringRedisTemplate.opsForValue().get(key);
                if (view != null) {
                    plannerRepository.updateViews(Long.valueOf(view), plannerId);
                }
                stringRedisTemplate.delete(key);
            }
        }
    }

}

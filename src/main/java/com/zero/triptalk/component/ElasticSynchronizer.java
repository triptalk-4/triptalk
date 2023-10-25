package com.zero.triptalk.component;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.querydsl.core.Tuple;
import com.zero.triptalk.planner.entity.PlannerDetailDocument;
import com.zero.triptalk.planner.entity.PlannerDocument;
import com.zero.triptalk.planner.repository.CustomPlannerDetailRepository;
import com.zero.triptalk.planner.repository.PlannerDetailSearchRepository;
import com.zero.triptalk.planner.repository.PlannerSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.zero.triptalk.planner.entity.QPlanner.planner;

@Slf4j
@RequiredArgsConstructor
@Component
public class ElasticSynchronizer {

    private final PlannerSearchRepository plannerSearchRepository;
    private final CustomPlannerDetailRepository customPlannerDetailRepository;
    private final PlannerDetailSearchRepository plannerDetailSearchRepository;
    private final ElasticsearchClient elasticsearchClient;

    @Scheduled(cron = "${scheduler.elasticsearch}")
    public void savePlannersToElasticSearch() {

        List<LocalDateTime> now = getNow();
        List<Tuple> planners =
                customPlannerDetailRepository.getPlannerListByLikeAndViewUpdateDt(now.get(0), now.get(1));
        if (!planners.isEmpty()) {

            List<PlannerDocument> plannerDocuments = PlannerDocument.ofTuple(planners);
            plannerSearchRepository.saveAll(plannerDocuments);
            log.info(LocalDateTime.now() + "=====================");
            log.info(now.get(0) + " 부터 " + now.get(1) + " 까지 PlannerDocument 저장완료. 총 : " +
                    plannerDocuments.size() + "개");

            List<Long> ids = planners.stream().map(x -> Objects.requireNonNull(
                    x.get(planner)).getPlannerId()).collect(Collectors.toList());
            savePlannerDetailsToElasticSearch(ids, now.get(0), now.get(1));


        }

    }

    public void savePlannerDetailsToElasticSearch(List<Long> ids, LocalDateTime from, LocalDateTime to) {

        List<Tuple> plannerDetails = customPlannerDetailRepository.getPlannerDetailListByPlannerId(ids);
        List<PlannerDetailDocument> plannerDetailDocuments = PlannerDetailDocument.ofTuple(plannerDetails);
        plannerDetailSearchRepository.saveAll(plannerDetailDocuments);
        log.info(LocalDateTime.now() + "=====================");
        log.info(from + " 부터 " + to + " 까지 PlannerDetailDocument 저장완료. 총 : " +
                                                                    plannerDetailDocuments.size() + "개");

    }

    private List<LocalDateTime> getNow() {

        LocalDateTime now = LocalDateTime.now();
        LocalTime time = LocalTime.of(now.getHour(), now.getMinute(), 0);
        LocalDateTime from = LocalDateTime.of(now.toLocalDate(), time.minusMinutes(1));
        LocalDateTime to = LocalDateTime.of(now.toLocalDate(), time.minusSeconds(1));

        return List.of(from, to);
    }

}

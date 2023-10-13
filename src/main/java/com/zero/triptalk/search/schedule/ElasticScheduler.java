package com.zero.triptalk.search.schedule;

import com.zero.triptalk.like.entity.DetailPlannerLikeDocument;
import com.zero.triptalk.like.entity.PlannerLike;
import com.zero.triptalk.like.entity.PlannerLikeDocument;
import com.zero.triptalk.like.repository.DetailPlannerLikeSearchRepository;
import com.zero.triptalk.like.repository.PlannerLikeRepository;
import com.zero.triptalk.like.repository.PlannerLikeSearchRepository;
import com.zero.triptalk.search.repository.CustomElasticRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class ElasticScheduler {

    LocalDateTime now = LocalDateTime.now();
    LocalTime time = LocalTime.of(now.getHour(), 0);
    LocalDateTime from = LocalDateTime.of(now.toLocalDate(), time.minusHours(1));
    LocalDateTime to = LocalDateTime.of(now.toLocalDate(), time.minusSeconds(1));

    private final PlannerLikeRepository plannerLikeRepository;
    private final PlannerLikeSearchRepository plannerLikeSearchRepository;
    private final DetailPlannerLikeSearchRepository detailPlannerLikeSearchRepository;
    private final CustomElasticRepository customElasticRepository;

    @Scheduled(cron = "${scheduler.elasticsearch}")
    public void savePlannersByLikesUpdateDt() {

        List<PlannerLike> plannerLikes = plannerLikeRepository.findAllByLikeDtBetween(from, to);

        List<PlannerLikeDocument> documents = plannerLikes.stream().map(PlannerLikeDocument::ofEntity)
                                                                        .collect(Collectors.toList());
        plannerLikeSearchRepository.saveAll(documents);
        log.info(LocalDateTime.now() + "=====================");
        log.info(from + " 부터 " + to + " 까지 PlannerLikeDocument 저장완료. 총 : " + documents.size() + "개");
    }

    @Scheduled(cron = "${scheduler.elasticsearch}")
    public void saveDetailPlannerByDateAndViewsAndLikesUpdateDt() {

        List<DetailPlannerLikeDocument> documents =
                customElasticRepository.findAllByDateAndViewsAndLikesUpdateDt(from, to);

        detailPlannerLikeSearchRepository.saveAll(documents);
        log.info(LocalDateTime.now() + "=====================");
        log.info(from + " 부터 " + to + " 까지 DetailPlannerLikeDocuments 저장완료. 총 : " + documents.size() + "개");
    }

}

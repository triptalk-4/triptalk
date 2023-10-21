package com.zero.triptalk.planner.repository;

import com.zero.triptalk.planner.entity.PlannerDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PlannerSearchRepository extends ElasticsearchRepository<PlannerDocument, Long> {
    List<PlannerDocument> findTop6ByOrderByLikesDesc();
    @Query("{\"match\": {\"user.userId\": \"?0\"}}")
    List<PlannerDocument> findAllByUser(Long userId);
}

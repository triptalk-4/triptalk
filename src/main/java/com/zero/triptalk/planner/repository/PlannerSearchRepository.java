package com.zero.triptalk.planner.repository;

import com.zero.triptalk.like.entity.PlannerDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PlannerSearchRepository extends ElasticsearchRepository<PlannerDocument, Long> {
    List<PlannerDocument> findTop6ByOrderByLikesDesc();
}

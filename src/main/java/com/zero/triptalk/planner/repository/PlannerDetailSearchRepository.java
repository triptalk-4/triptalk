package com.zero.triptalk.planner.repository;

import com.zero.triptalk.planner.entity.PlannerDetailDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannerDetailSearchRepository extends ElasticsearchRepository<PlannerDetailDocument, Long> {
}

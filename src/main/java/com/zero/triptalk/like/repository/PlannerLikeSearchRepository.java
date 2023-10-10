package com.zero.triptalk.like.repository;

import com.zero.triptalk.like.entity.PlannerLikeDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PlannerLikeSearchRepository extends ElasticsearchRepository<PlannerLikeDocument, Long> {
    List<PlannerLikeDocument> findTop6ByOrderByLikeCountDesc();
}

package com.zero.triptalk.like.repository;

import com.zero.triptalk.like.entity.DetailPlannerLikeDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DetailPlannerLikeSearchRepository extends ElasticsearchRepository<DetailPlannerLikeDocument, Long> {
}

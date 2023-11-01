package com.zero.triptalk.planner.repository;

import com.zero.triptalk.planner.entity.PlannerDetailDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomPlannerDetailSearchRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    public List<PlannerDetailDocument> searchByPlace(GeoPoint point, GeoPoint point2,Pageable pageable) {

        Criteria criteria = Criteria.where("point").boundedBy(point, point2);

        CriteriaQuery query = CriteriaQuery.builder(criteria)
                .withPageable(pageable)
                .build();

        return elasticsearchOperations.search(query, PlannerDetailDocument.class)
                                .stream().map(SearchHit::getContent).collect(Collectors.toList());


    }
}

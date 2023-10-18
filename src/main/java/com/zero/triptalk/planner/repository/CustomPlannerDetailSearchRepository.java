package com.zero.triptalk.planner.repository;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import com.zero.triptalk.exception.code.SearchErrorCode;
import com.zero.triptalk.exception.custom.SearchException;
import com.zero.triptalk.planner.entity.PlannerDetailDocument;
import com.zero.triptalk.search.type.SearchType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
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

    public List<PlannerDetailDocument> searchByRegionAndSearchType(String region, String searchType, Pageable pageable) {

        Criteria criteria = Criteria.where("place").contains(region);

        CriteriaQuery query = CriteriaQuery.builder(criteria)
                .withSort(Sort.by(SearchType.getSearchType(searchType)).descending())
                .withPageable(pageable)
                .build();

        try {
            return elasticsearchOperations.search(query, PlannerDetailDocument.class)
                                    .stream().map(SearchHit::getContent).collect(Collectors.toList());
        } catch (ElasticsearchException e) {
            log.error(getClass() + " 의 Exception -> " + e.getMessage());
            throw new SearchException(SearchErrorCode.RESULT_NOT_FOUND);
        }

    }

}

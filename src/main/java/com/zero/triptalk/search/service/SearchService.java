package com.zero.triptalk.search.service;

import com.zero.triptalk.like.dto.response.DetailPlannerSearchResponse;
import com.zero.triptalk.like.dto.response.PlannerLikeSearchResponse;
import com.zero.triptalk.like.entity.DetailPlannerLikeDocument;
import com.zero.triptalk.like.entity.PlannerLikeDocument;
import com.zero.triptalk.like.repository.DetailPlannerLikeSearchCustomRepository;
import com.zero.triptalk.like.repository.PlannerLikeSearchRepository;
import com.zero.triptalk.user.dto.UserSearchResponse;
import com.zero.triptalk.user.entity.UserDocument;
import com.zero.triptalk.user.repository.UserSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final PlannerLikeSearchRepository plannerLikeSearchRepository;
    private final DetailPlannerLikeSearchCustomRepository detailPlannerLikeSearchCustomRepository;
    private final UserSearchRepository userSearchRepository;

    public List<PlannerLikeSearchResponse> getTop6PlannersWithLikes() {

        List<PlannerLikeDocument> top6ByOrderByLikeCountDesc =
                plannerLikeSearchRepository.findTop6ByOrderByLikeCountDesc();

        return top6ByOrderByLikeCountDesc.stream().map(PlannerLikeSearchResponse::ofEntity)
                                                           .collect(Collectors.toList());
    }

    public List<DetailPlannerSearchResponse> searchByRegionAnySort(
                                        String region, String searchType, Pageable pageable) {

        List<DetailPlannerLikeDocument> searchResponses =
                detailPlannerLikeSearchCustomRepository.searchByRegionAndSearchType(
                                                            region, searchType, pageable);

        return searchResponses.stream().map(DetailPlannerSearchResponse::ofEntity)
                                                            .collect(Collectors.toList());

    }

    public List<UserSearchResponse> getUserNicknameList(String keyword, Pageable pageable) {

        List<UserDocument> documents = userSearchRepository.findByNicknameContains(keyword, pageable);

        if (documents.isEmpty()) {
            return Collections.emptyList();
        }

        return documents.stream().map(UserSearchResponse::ofDocument).collect(Collectors.toList());
    }
}

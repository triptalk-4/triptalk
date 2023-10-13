package com.zero.triptalk.search.service;

import com.zero.triptalk.like.dto.response.DetailPlannerSearchResponse;
import com.zero.triptalk.like.dto.response.PlannerLikeSearchResponse;
import com.zero.triptalk.like.entity.DetailPlannerLikeDocument;
import com.zero.triptalk.like.entity.PlannerLikeDocument;
import com.zero.triptalk.like.repository.*;
import com.zero.triptalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final PlannerLikeSearchRepository plannerLikeSearchRepository;
    private final DetailPlannerLikeSearchCustomRepository detailPlannerLikeSearchCustomRepository;
    private final UserRepository userRepository;

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

    public List<String> getUserNicknameList(String keyword) {

        List<String> nicknames= userRepository.findByNicknameContains(keyword);

        if (nicknames.isEmpty()) {
            return Collections.emptyList();
        }

        return nicknames;
    }
}

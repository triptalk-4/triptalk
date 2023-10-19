package com.zero.triptalk.search.service;

import com.zero.triptalk.planner.dto.PlannerDetailSearchResponse;
import com.zero.triptalk.planner.dto.PlannerSearchResponse;
import com.zero.triptalk.planner.entity.PlannerDetailDocument;
import com.zero.triptalk.planner.entity.PlannerDocument;
import com.zero.triptalk.planner.repository.CustomPlannerDetailSearchRepository;
import com.zero.triptalk.planner.repository.PlannerSearchRepository;
import com.zero.triptalk.user.dto.UserInfoSearchResponse;
import com.zero.triptalk.user.dto.UserSearchResponse;
import com.zero.triptalk.user.entity.UserDocument;
import com.zero.triptalk.user.repository.UserSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final PlannerSearchRepository plannerSearchRepository;
    private final CustomPlannerDetailSearchRepository customPlannerDetailSearchRepository;
    private final UserSearchRepository userSearchRepository;

    public List<PlannerSearchResponse> getTop6PlannersWithLikes() {

        List<PlannerDocument> top6ByOrderByLikeCountDesc =
                plannerSearchRepository.findTop6ByOrderByLikesDesc();

        return top6ByOrderByLikeCountDesc.stream().map(PlannerSearchResponse::ofEntity)
                                                           .collect(Collectors.toList());
    }

    public List<PlannerDetailSearchResponse> searchByRegionAnySort(
                                        String region, String searchType, Pageable pageable) {

        List<PlannerDetailDocument> searchResponses =
                customPlannerDetailSearchRepository.searchByRegionAndSearchType(
                                                            region, searchType, pageable);

        return searchResponses.stream().map(PlannerDetailSearchResponse::ofEntity)
                                                            .collect(Collectors.toList());

    }

    public List<UserSearchResponse> getUserNicknameList(String keyword, Pageable pageable) {

        List<UserDocument> documents = userSearchRepository.findByNicknameContains(keyword, pageable);

        return UserSearchResponse.ofDocument(documents);
    }

    public UserInfoSearchResponse searchByUserId(Long userId) {

        List<PlannerDocument> plannerDocuments = plannerSearchRepository.findAllByUser(userId);

        return UserInfoSearchResponse.ofDocument(plannerDocuments);
    }
}

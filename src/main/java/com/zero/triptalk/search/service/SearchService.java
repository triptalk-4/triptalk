package com.zero.triptalk.search.service;

import com.zero.triptalk.exception.code.SearchErrorCode;
import com.zero.triptalk.exception.code.UserErrorCode;
import com.zero.triptalk.exception.custom.SearchException;
import com.zero.triptalk.exception.custom.UserException;
import com.zero.triptalk.planner.dto.response.PlannerDetailSearchResponse;
import com.zero.triptalk.planner.dto.response.PlannerSearchResponse;
import com.zero.triptalk.planner.entity.PlannerDetailDocument;
import com.zero.triptalk.planner.entity.PlannerDocument;
import com.zero.triptalk.planner.repository.CustomPlannerSearchRepository;
import com.zero.triptalk.planner.repository.PlannerSearchRepository;
import com.zero.triptalk.user.dto.UserInfoSearchResponse;
import com.zero.triptalk.user.dto.UserSearchResponse;
import com.zero.triptalk.user.entity.UserDocument;
import com.zero.triptalk.user.repository.UserSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.NoSuchIndexException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final PlannerSearchRepository plannerSearchRepository;
    private final CustomPlannerSearchRepository customPlannerSearchRepository;
    private final UserSearchRepository userSearchRepository;

    public List<PlannerSearchResponse> getTop6PlannersWithLikes() throws NoSuchIndexException {

        List<PlannerDocument> top6ByOrderByLikeCountDesc =
                plannerSearchRepository.findTop6ByOrderByLikesDesc();

        return top6ByOrderByLikeCountDesc.stream().map(PlannerSearchResponse::ofEntity)
                                                           .collect(Collectors.toList());
    }

    public List<PlannerDetailSearchResponse> searchByRegionAnySort(
                                        String region, String searchType, Pageable pageable)
                                                                throws NoSuchIndexException {

        if (region.isEmpty() || region.trim().equals("")) {
            throw new SearchException(SearchErrorCode.INVALID_REQUEST);
        }

        List<PlannerDetailDocument> searchResponses =
                customPlannerSearchRepository.searchByRegionAndSearchType(
                                                            region, searchType, pageable);

        return searchResponses.stream().map(PlannerDetailSearchResponse::ofEntity)
                                                            .collect(Collectors.toList());

    }

    public List<UserSearchResponse> getUserNicknameList(String keyword)
                                                                throws NoSuchIndexException {

        List<UserDocument> documents = userSearchRepository.findByNicknameContains(keyword);

        return UserSearchResponse.ofDocument(documents);
    }

    public UserInfoSearchResponse searchByUserId(Long userId,Pageable pageable)
                                                                throws NoSuchIndexException {

        UserDocument userDocument = userSearchRepository.findById(userId).orElseThrow(() ->
                                                new UserException(UserErrorCode.USER_NOT_FOUND));

        List<PlannerDocument> plannerDocuments =
                        customPlannerSearchRepository.getAllByUserId(userId, pageable);

        return UserInfoSearchResponse.ofDocument(userDocument, plannerDocuments);
    }
}

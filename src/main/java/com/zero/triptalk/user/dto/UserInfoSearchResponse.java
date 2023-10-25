package com.zero.triptalk.user.dto;

import com.zero.triptalk.planner.entity.PlannerDocument;
import com.zero.triptalk.user.entity.UserDocument;
import com.zero.triptalk.user.response.MyPlannerBoardResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoSearchResponse {

    private Long userId;
    private String nickname;
    private String aboutMe;
    private String profile;
    private List<MyPlannerBoardResponse> planners;

    @Builder
    public UserInfoSearchResponse(Long userId, String nickname, String aboutMe, String profile, List<MyPlannerBoardResponse> planners) {
        this.userId = userId;
        this.nickname = nickname;
        this.aboutMe = aboutMe;
        this.profile = profile;
        this.planners = planners;
    }

    public static UserInfoSearchResponse ofDocument(UserDocument userDocument, List<PlannerDocument> plannerDocuments) {

        List<MyPlannerBoardResponse> list = Collections.emptyList();

        if (!plannerDocuments.isEmpty()) {
            list = plannerDocuments.stream().map(MyPlannerBoardResponse::ofDocument).collect(Collectors.toList());
        }

        return UserInfoSearchResponse.builder()
                                        .userId(userDocument.getUserId())
                                        .nickname(userDocument.getNickname())
                                        .aboutMe(userDocument.getAboutMe())
                                        .profile(userDocument.getProfile())
                                        .planners(list)
                                        .build();

    }
}

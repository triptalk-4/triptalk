package com.zero.triptalk.user.dto;

import com.zero.triptalk.planner.entity.PlannerDocument;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.response.MyPlannerBoardResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public static UserInfoSearchResponse ofDocument(List<PlannerDocument> plannerDocuments) {
        List<MyPlannerBoardResponse> list =
                plannerDocuments.stream().map(MyPlannerBoardResponse::ofDocument).collect(Collectors.toList());
        UserEntity user = plannerDocuments.get(0).getUser();

        return UserInfoSearchResponse.builder()
                                        .userId(user.getUserId())
                                        .nickname(user.getNickname())
                                        .aboutMe(user.getAboutMe())
                                        .profile(user.getProfile())
                                        .planners(list)
                                        .build();

    }
}

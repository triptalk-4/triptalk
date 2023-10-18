package com.zero.triptalk.user.dto;

import com.zero.triptalk.user.entity.UserDocument;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSearchResponse {

    private Long userId;
    private String nickname;
    private String aboutMe;
    private String profile;

    @Builder
    public UserSearchResponse(Long userId, String nickname, String aboutMe, String profile) {
        this.userId = userId;
        this.nickname = nickname;
        this.aboutMe = aboutMe;
        this.profile = profile;
    }

    public static List<UserSearchResponse> ofDocument(List<UserDocument> documents) {

        if (documents.isEmpty()) {
            return Collections.emptyList();
        }

        List<UserSearchResponse> list = new ArrayList<>();
        for (UserDocument x : documents) {
            list.add(UserSearchResponse.builder()
                                        .userId(x.getUserId())
                                        .nickname(x.getNickname())
                                        .aboutMe(x.getAboutMe())
                                        .profile(x.getProfile())
                                        .build());
        }

        return list;
    }
}

package com.zero.triptalk.user.dto;

import com.zero.triptalk.user.entity.UserDocument;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSearchResponse {

    private Long userId;
    private String nickname;
    private String aboutMe;

    @Builder
    public UserSearchResponse(Long userId, String nickname, String aboutMe) {
        this.userId = userId;
        this.nickname = nickname;
        this.aboutMe = aboutMe;
    }

    public static UserSearchResponse ofDocument(UserDocument document) {
        return UserSearchResponse.builder()
                .userId(document.getUserId())
                .nickname(document.getNickname())
                .aboutMe(document.getAboutMe())
                .build();
    }
}

package com.zero.triptalk.like.dto.response;

import com.zero.triptalk.like.entity.PlannerDocument;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlannerLikeSearchResponse {

    private Long plannerId;
    private String title;
    private String thumbnail;
    private String nickname;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long views;
    private Long likeCount;

    @Builder
    public PlannerLikeSearchResponse(Long plannerLikeId, Long plannerId, String title, String thumbnail, String nickname, LocalDateTime startDate, LocalDateTime endDate, Long views, Long likeCount) {
        this.plannerId = plannerId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.nickname = nickname;
        this.startDate = startDate;
        this.endDate = endDate;
        this.views = views;
        this.likeCount = likeCount;
    }

    public static PlannerLikeSearchResponse ofEntity(PlannerDocument document) {

        return PlannerLikeSearchResponse.builder()
                .plannerId(document.getPlannerId())
                .title(document.getTitle())
                .thumbnail(document.getThumbnail())
                .nickname(document.getNickname())
                .startDate(document.getStartDate())
                .endDate(document.getEndDate())
                .views(document.getViews())
                .likeCount(document.getLikes())
                .build();
    }
}

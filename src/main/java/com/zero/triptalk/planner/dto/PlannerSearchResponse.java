package com.zero.triptalk.planner.dto;

import com.zero.triptalk.planner.entity.PlannerDocument;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlannerSearchResponse {

    private Long plannerId;
    private String title;
    private String thumbnail;
    private String nickname;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long views;
    private Long likeCount;

    @Builder
    public PlannerSearchResponse(Long plannerLikeId, Long plannerId, String title, String thumbnail, String nickname, LocalDateTime startDate, LocalDateTime endDate, Long views, Long likeCount) {
        this.plannerId = plannerId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.nickname = nickname;
        this.startDate = startDate;
        this.endDate = endDate;
        this.views = views;
        this.likeCount = likeCount;
    }

    public static PlannerSearchResponse ofEntity(PlannerDocument document) {

        return PlannerSearchResponse.builder()
                .plannerId(document.getPlannerId())
                .title(document.getTitle())
                .thumbnail(document.getThumbnail())
                .nickname(document.getUser().getNickname())
                .startDate(document.getStartDate())
                .endDate(document.getEndDate())
                .views(document.getViews())
                .likeCount(document.getLikes())
                .build();
    }
}

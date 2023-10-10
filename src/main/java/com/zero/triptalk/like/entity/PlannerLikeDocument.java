package com.zero.triptalk.like.entity;

import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.planner.type.PlannerStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "planner")
public class PlannerLikeDocument {
    @Id
    private Long plannerLikeId;
    private Long plannerId;
    private String title;
    private String thumbnail;
    private String nickname;
    private PlannerStatus plannerStatus;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime startDate;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime endDate;
    private Long views;
    private Long likeCount;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime likeDt;

    @Builder
    public PlannerLikeDocument(Long plannerLikeId, Long plannerId, String title, String thumbnail, String nickname, PlannerStatus plannerStatus, LocalDateTime startDate, LocalDateTime endDate, Long views, Long likeCount, LocalDateTime likeDt) {
        this.plannerLikeId = plannerLikeId;
        this.plannerId = plannerId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.nickname = nickname;
        this.plannerStatus = plannerStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.views = views;
        this.likeCount = likeCount;
        this.likeDt = likeDt;
    }

    public static PlannerLikeDocument ofEntity(PlannerLike plannerLike) {

        Planner planner = plannerLike.getPlanner();

        return PlannerLikeDocument.builder()
                .plannerLikeId(plannerLike.getPlannerLikeId())
                .plannerId(planner.getPlannerId())
                .title(planner.getTitle())
                .thumbnail(planner.getThumbnail())
                .nickname(planner.getUser().getNickname())
                .plannerStatus(planner.getPlannerStatus())
                .startDate(planner.getStartDate())
                .endDate(planner.getEndDate())
                .views(planner.getViews())
                .likeCount(plannerLike.getLikeCount())
                .likeDt(plannerLike.getLikeDt())
                .build();
    }
}

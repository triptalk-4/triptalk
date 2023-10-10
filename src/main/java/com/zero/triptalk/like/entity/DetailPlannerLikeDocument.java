package com.zero.triptalk.like.entity;

import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.planner.entity.PlannerDetail;
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
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "detail_planner")
public class DetailPlannerLikeDocument {
    @Id
    private Long detailPlannerLikeId;
    private Long plannerDetailId;
    private Long userId;
    private String description;
    private List<String> images;
    private String place;
    private Planner planner;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime date;
    private Long views;
    private Long likeCount;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime likeDt;

    @Builder
    public DetailPlannerLikeDocument(Long detailPlannerLikeId, Long plannerDetailId, Long userId, String description, List<String> images, String place, Planner planner, LocalDateTime date, Long views, Long likeCount, LocalDateTime likeDt) {
        this.detailPlannerLikeId = detailPlannerLikeId;
        this.plannerDetailId = plannerDetailId;
        this.userId = userId;
        this.description = description;
        this.images = images;
        this.place = place;
        this.planner = planner;
        this.date = date;
        this.views = views;
        this.likeCount = likeCount;
        this.likeDt = likeDt;
    }

    public static DetailPlannerLikeDocument ofEntity(DetailPlannerLike detailPlannerLike) {

        PlannerDetail plannerDetail = detailPlannerLike.getPlannerDetail();

        return DetailPlannerLikeDocument.builder()
                .detailPlannerLikeId(detailPlannerLike.detailPlannerLikeId)
                .plannerDetailId(plannerDetail.getPlannerDetailId())
                .userId(plannerDetail.getUserId())
                .description(plannerDetail.getDescription())
                .images(plannerDetail.getImages())
                .place(plannerDetail.getPlace().getRoadAddress())
                .planner(plannerDetail.getPlanner())
                .date(plannerDetail.getDate())
                .views(plannerDetail.getViews())
                .likeCount(detailPlannerLike.getLikeCount())
                .likeDt(detailPlannerLike.getLikeDt())
                .build();
    }
}

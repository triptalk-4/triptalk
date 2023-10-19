package com.zero.triptalk.planner.entity;

import com.querydsl.core.Tuple;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.zero.triptalk.like.entity.QPlannerLike.plannerLike;
import static com.zero.triptalk.planner.entity.QPlannerDetail.plannerDetail;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "planner_detail")
public class PlannerDetailDocument {
    @Id
    @Field(type = FieldType.Keyword)
    private Long plannerDetailId;
    private String nickname;
    private String description;
    private List<String> images;
    private String place;
    @Field(type = FieldType.Keyword)
    private Long plannerId;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime date;
    @Field(type = FieldType.Integer)
    private Long views;
    @Field(type = FieldType.Integer)
    private Long likes;

    @Builder
    public PlannerDetailDocument(Long plannerDetailId, String nickname, String description, List<String> images, String place, Long plannerId, LocalDateTime date, Long views, Long likes) {
        this.plannerDetailId = plannerDetailId;
        this.nickname = nickname;
        this.description = description;
        this.images = images;
        this.place = place;
        this.plannerId = plannerId;
        this.date = date;
        this.views = views;
        this.likes = likes;
    }

    public static List<PlannerDetailDocument> ofEntity(List<PlannerDetail> plannerDetails) {

        if(plannerDetails.isEmpty()) {
            return Collections.emptyList();
        }

        List<PlannerDetailDocument> list = new ArrayList<>();
        for(PlannerDetail x : plannerDetails) {
            list.add(PlannerDetailDocument.builder()
                                            .plannerDetailId(x.getPlannerDetailId())
                                            .nickname(x.getPlanner().getUser().getNickname())
                                            .description(x.getDescription())
                                            .images(x.getImages())
                                            .place(x.getPlace().getRoadAddress())
                                            .plannerId(x.getPlanner().getPlannerId())
                                            .date(x.getDate())
                                            .build());
        }
        return list;
    }

    public static List<PlannerDetailDocument> ofTuple(List<Tuple> tuples) {

        if(tuples.isEmpty()) {
            return Collections.emptyList();
        }

        List<PlannerDetailDocument> list = new ArrayList<>();
        for(Tuple x : tuples) {
            PlannerDetail xPlannerDetail = x.get(plannerDetail);

            list.add(PlannerDetailDocument.builder()
                            .plannerDetailId(Objects.requireNonNull(xPlannerDetail).getPlannerDetailId())
                            .nickname(xPlannerDetail.getPlanner().getUser().getNickname())
                            .description(xPlannerDetail.getDescription())
                            .images(xPlannerDetail.getImages())
                            .place(xPlannerDetail.getPlace().getRoadAddress())
                            .plannerId(xPlannerDetail.getPlanner().getPlannerId())
                            .date(xPlannerDetail.getDate())
                            .views(xPlannerDetail.getPlanner().getViews())
                            .likes(x.get(plannerLike.likeCount))
                            .build());
        }

        return list;
    }

}

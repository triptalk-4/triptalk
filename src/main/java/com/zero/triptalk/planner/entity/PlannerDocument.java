package com.zero.triptalk.planner.entity;

import com.querydsl.core.Tuple;
import com.zero.triptalk.user.entity.UserEntity;
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
import static com.zero.triptalk.planner.entity.QPlanner.planner;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "planner")
public class PlannerDocument {
    @Id
    @Field(type = FieldType.Keyword)
    private Long plannerId;
    private String title;
    private String thumbnail;
    private UserEntity user;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime startDate;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime endDate;
    @Field(type = FieldType.Integer)
    private Long views;
    @Field(type = FieldType.Integer)
    private Long likes;

    @Builder
    public PlannerDocument(Long plannerId, String title, String thumbnail, UserEntity user, LocalDateTime startDate, LocalDateTime endDate, Long views, Long likes) {
        this.plannerId = plannerId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.views = views;
        this.likes = likes;
    }

    public static List<PlannerDocument> ofTuple(List<Tuple> tuples) {

        if(tuples.isEmpty()) {
            return Collections.emptyList();
        }

        List<PlannerDocument> list = new ArrayList<>();
        for(Tuple x : tuples) {
            Planner xPlanner = x.get(planner);

            list.add(PlannerDocument.builder()
                                    .plannerId(Objects.requireNonNull(xPlanner).getPlannerId())
                                    .title(xPlanner.getTitle())
                                    .thumbnail(xPlanner.getThumbnail())
                                    .user(xPlanner.getUser())
                                    .startDate(xPlanner.getStartDate())
                                    .endDate(xPlanner.getEndDate())
                                    .views(xPlanner.getViews())
                                    .likes(x.get(plannerLike.likeCount))
                                    .build());

        }

        return list;
    }
}

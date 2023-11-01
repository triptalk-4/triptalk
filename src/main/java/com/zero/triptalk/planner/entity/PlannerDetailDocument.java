package com.zero.triptalk.planner.entity;

import com.querydsl.core.Tuple;
import com.zero.triptalk.place.entity.Place;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.zero.triptalk.like.entity.QPlannerLike.plannerLike;
import static com.zero.triptalk.planner.entity.QPlannerDetail.plannerDetail;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "planner_detail", writeTypeHint = WriteTypeHint.FALSE)
public class PlannerDetailDocument {
    @Id
    @Field(type = FieldType.Keyword)
    private Long plannerDetailId;
    @Field(type = FieldType.Keyword)
    private Long plannerId;
    private String nickname;
    private String placeName;
    private String roadAddress;
    private String addressName;
    @GeoPointField
    private GeoPoint point;
    private Long placeLike;
    private String description;
    private List<String> images;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime date;
    @Field(type = FieldType.Integer, nullValue = "0")
    private Long views;
    @Field(type = FieldType.Integer, nullValue = "0")
    private Long likes;

    @Builder
    public PlannerDetailDocument(Long plannerDetailId, Long plannerId, String nickname, String placeName, String roadAddress, String addressName, GeoPoint point, Long placeLike, String description, List<String> images, LocalDateTime date, Long views, Long likes) {
        this.plannerDetailId = plannerDetailId;
        this.plannerId = plannerId;
        this.nickname = nickname;
        this.placeName = placeName;
        this.roadAddress = roadAddress;
        this.addressName = addressName;
        this.point = point;
        this.placeLike = placeLike;
        this.description = description;
        this.images = images;
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
            Place place = x.getPlace();

            list.add(PlannerDetailDocument.builder()
                                            .plannerDetailId(x.getPlannerDetailId())
                                            .nickname(x.getPlanner().getUser().getNickname())
                                            .description(x.getDescription())
                                            .images(x.getImages())
                                            .placeName(place.getPlaceName())
                                            .roadAddress(place.getRoadAddress())
                                            .addressName(place.getAddressName())
                                            .point(new GeoPoint(place.getLatitude(), place.getLongitude()))
                                            .placeLike(place.getPlaceLike())
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
            Place place = Objects.requireNonNull(xPlannerDetail).getPlace();

            list.add(PlannerDetailDocument.builder()
                            .plannerDetailId(Objects.requireNonNull(xPlannerDetail).getPlannerDetailId())
                            .nickname(xPlannerDetail.getPlanner().getUser().getNickname())
                            .description(xPlannerDetail.getDescription())
                            .images(xPlannerDetail.getImages())
                            .placeName(place.getPlaceName())
                            .roadAddress(place.getRoadAddress())
                            .addressName(place.getAddressName())
                            .point(new GeoPoint(place.getLatitude(), place.getLongitude()))
                            .placeLike(place.getPlaceLike())
                            .plannerId(xPlannerDetail.getPlanner().getPlannerId())
                            .date(xPlannerDetail.getDate())
                            .views(xPlannerDetail.getPlanner().getViews())
                            .likes(x.get(plannerLike.likeCount))
                            .build());
        }

        return list;
    }

}

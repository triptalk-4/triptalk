package com.zero.triptalk.planner.dto.response;

import com.zero.triptalk.planner.entity.PlannerDetailDocument;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlannerDetailSearchResponse {

    private Long plannerDetailId;
    private String nickname;
    private String profile;
    private String description;
    private List<String> image;
    private String place;
    private Double lat;
    private Double lon;
    private LocalDateTime date;
    private Long views;
    private Long likeCount;

    @Builder
    public PlannerDetailSearchResponse(Long plannerDetailId, String nickname, String profile, String description, List<String> image, String place, Double lat, Double lon, LocalDateTime date, Long views, Long likeCount) {
        this.plannerDetailId = plannerDetailId;
        this.nickname = nickname;
        this.profile = profile;
        this.description = description;
        this.image = image;
        this.place = place;
        this.lat = lat;
        this.lon = lon;
        this.date = date;
        this.views = views;
        this.likeCount = likeCount;
    }

    public static PlannerDetailSearchResponse ofEntity(PlannerDetailDocument document) {

        return PlannerDetailSearchResponse.builder()
                .plannerDetailId(document.getPlannerDetailId())
                .nickname(document.getNickname())
                .profile(document.getProfile())
                .description(document.getDescription())
                .image(document.getImages())
                .place(document.getRoadAddress())
                .lat(document.getPoint().lat())
                .lon(document.getPoint().lon())
                .date(document.getDate())
                .views(document.getViews())
                .likeCount(document.getLikes())
                .build();
    }
}

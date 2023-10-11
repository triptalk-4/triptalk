package com.zero.triptalk.like.dto.response;

import com.zero.triptalk.like.entity.DetailPlannerLikeDocument;
import com.zero.triptalk.planner.entity.Planner;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DetailPlannerSearchResponse {

    private Long plannerDetailId;
    private String nickname;
    private String description;
    private String image;
    private String place;
    private LocalDateTime date;
    private Long views;
    private Long likeCount;

    @Builder
    public DetailPlannerSearchResponse(Long plannerDetailId, String nickname, String description, String image, String place, LocalDateTime date, Long views, Long likeCount) {
        this.plannerDetailId = plannerDetailId;
        this.nickname = nickname;
        this.description = description;
        this.image = image;
        this.place = place;
        this.date = date;
        this.views = views;
        this.likeCount = likeCount;
    }

    public static DetailPlannerSearchResponse ofEntity(DetailPlannerLikeDocument document) {

        Planner planner = document.getPlanner();

        return DetailPlannerSearchResponse.builder()
                .plannerDetailId(document.getPlannerDetailId())
                .nickname(planner.getUser().getNickname())
                .description(document.getDescription())
                .image(getOneImage(document.getImages()))
                .place(document.getPlace())
                .date(document.getDate())
                .views(document.getViews())
                .likeCount(document.getLikeCount())
                .build();
    }

    private static String getOneImage(List<String> images) {

        if(images.isEmpty()) {
            return StringUtils.EMPTY;
        }

        return images.get(0);
    }
}

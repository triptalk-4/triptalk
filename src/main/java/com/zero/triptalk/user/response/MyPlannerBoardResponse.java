package com.zero.triptalk.user.response;

import com.zero.triptalk.planner.entity.PlannerDocument;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class MyPlannerBoardResponse {
    private Long plannerId;
    private String title;
    private String thumbnail;
    private Long views;
    private String createAt;
    private Long likeCount;

    @Builder
    public MyPlannerBoardResponse(Long plannerId, String title, String thumbnail, Long views, String createAt, Long likeCount) {
        this.plannerId = plannerId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.views = views;
        this.createAt = createAt;
        this.likeCount = likeCount;
    }

    public static MyPlannerBoardResponse ofDocument(PlannerDocument plannerDocument) {
        return MyPlannerBoardResponse.builder()
                .plannerId(plannerDocument.getPlannerId())
                .title(plannerDocument.getTitle())
                .thumbnail(plannerDocument.getThumbnail())
                .views(plannerDocument.getViews())
                .likeCount(plannerDocument.getLikes())
                .build();
    }
}

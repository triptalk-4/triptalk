package com.zero.triptalk.user.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikePlannerResponse {
    private Long plannerId;
    private String title;
    private String thumbnail;
    private Long views;
    private String createAt;
    private Long likeCount;
}

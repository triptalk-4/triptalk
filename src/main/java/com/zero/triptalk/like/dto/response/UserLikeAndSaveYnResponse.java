package com.zero.triptalk.like.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLikeAndSaveYnResponse {
    private String userSaveYn;
    private String userLikeYn;

}

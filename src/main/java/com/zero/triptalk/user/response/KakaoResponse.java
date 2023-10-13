package com.zero.triptalk.user.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoResponse {
    private String token;
    private String kakaoLoginOk;
}

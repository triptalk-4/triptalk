package com.zero.triptalk.user.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NicknameCheckRequest {
    private String nickname;

}

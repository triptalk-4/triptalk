package com.zero.triptalk.user.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String name;

    private String nickname;

    private String email ;

    private String password;

}


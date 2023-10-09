package com.zero.triptalk.user.request;

import lombok.*;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    private String email;
    String password;
}


package com.zero.triptalk.user.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailTokenRequest {
    private String token;
    private String password;
    private String email;

}

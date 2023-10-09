package com.zero.triptalk.user.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailCheckOkResponse {
    private String emailVerificationCompleted;
    private String emailVerificationFailed;
}

package com.zero.triptalk.user.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailCheckOkResponse {
    private String emailVerificationCompleted;
    private String emailVerificationFailed;
}

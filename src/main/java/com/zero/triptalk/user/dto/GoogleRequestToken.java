package com.zero.triptalk.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GoogleRequestToken {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String grantType;
    private String code;

}

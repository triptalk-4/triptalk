package com.zero.triptalk.auth;

import com.google.gson.Gson;
import com.zero.triptalk.auth.client.FeignClientGoogleAuth;
import com.zero.triptalk.auth.client.FeignClientGoogleUser;
import com.zero.triptalk.auth.dto.GoogleAuthResponse;
import com.zero.triptalk.auth.dto.GoogleRequestToken;
import com.zero.triptalk.auth.dto.GoogleUserInfoResponse;
import com.zero.triptalk.config.JwtService;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.enumType.UserLoginRole;
import com.zero.triptalk.user.enumType.UserTypeRole;
import com.zero.triptalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleLoginService {

    private final FeignClientGoogleAuth feignClientGoogleAuth;
    private final FeignClientGoogleUser feignClientGoogleUser;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;
    @Value("${GOOGLE_CLIENT_SECRET}")
    private String googleClientPw;
    @Value("${GOOGLE_REDIRECT_URI}")
    private String redirectUri;
    @Value("${GOOGLE_GRANT_TYPE}")
    private String grantType;
    @Value("${GOOGLE_SCOPE")
    private String scope;

    public String doSocialLogin(String code) {

        GoogleRequestToken googleRequestToken = GoogleRequestToken.builder()
                                                                .clientId(googleClientId)
                                                                .clientSecret(googleClientPw)
                                                                .code(code)
                                                                .grantType(grantType)
                                                                .redirectUri(redirectUri)
                                                                .build();

        ResponseEntity<String> accessToken = feignClientGoogleAuth.getAccessToken(googleRequestToken);

        Gson gson = new Gson();
        GoogleAuthResponse googleAuthResponse = gson.fromJson(accessToken.getBody(), GoogleAuthResponse.class);

        ResponseEntity<String> userInfo = feignClientGoogleUser.getUserInfo(googleAuthResponse.getAccess_token());
        GoogleUserInfoResponse googleUserInfoResponse = gson.fromJson(userInfo.getBody(), GoogleUserInfoResponse.class);

        Optional<UserEntity> optionalUser = userRepository.findByEmail(googleUserInfoResponse.getEmail());

        UserEntity user = optionalUser.orElseGet(() -> saveGoogleUser(googleUserInfoResponse));

        return jwtService.generateToken(user);
    }

    public String getLoginUrl() {

        return "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + googleClientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&scope=" + scope;
    }

    private UserEntity saveGoogleUser(GoogleUserInfoResponse userInfo) {

        String uuid = UUID.randomUUID().toString().substring(0,8);

        UserEntity user = UserEntity.builder()
                .UserType(UserTypeRole.USER)
                .userLoginRole(UserLoginRole.GOOGLE_USER_LOGIN)
                .name(userInfo.getName())
                .email(userInfo.getEmail())
                .nickname("GOOGLE" + uuid)
                .password(BCrypt.hashpw(uuid, BCrypt.gensalt()))
                .build();

        userRepository.saveAndFlush(user);

        return user;
    }
}

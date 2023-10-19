package com.zero.triptalk.user.service;

import com.google.gson.Gson;
import com.zero.triptalk.user.client.FeignClientGoogleAuth;
import com.zero.triptalk.user.client.FeignClientGoogleUser;
import com.zero.triptalk.user.dto.GoogleAuthResponse;
import com.zero.triptalk.user.dto.GoogleRequestToken;
import com.zero.triptalk.user.dto.GoogleUserInfoResponse;
import com.zero.triptalk.config.JwtService;
import com.zero.triptalk.user.entity.UserDocument;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.enumType.UserLoginRole;
import com.zero.triptalk.user.enumType.UserTypeRole;
import com.zero.triptalk.user.repository.UserRepository;
import com.zero.triptalk.user.repository.UserSearchRepository;
import com.zero.triptalk.user.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleAuthService {

    private final FeignClientGoogleAuth feignClientGoogleAuth;
    private final FeignClientGoogleUser feignClientGoogleUser;
    private final UserRepository userRepository;
    private final UserSearchRepository userSearchRepository;
    private final JwtService jwtService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientPw;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
    private String grantType;
    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String scope;
    @Value("${cloud.aws.image}")
    private String profile;

    public AuthenticationResponse doSocialLogin(String code) {

        byte[] encodeCode = code.getBytes();
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodeByte = decoder.decode(encodeCode);

        GoogleRequestToken googleRequestToken = GoogleRequestToken.builder()
                                                                .clientId(googleClientId)
                                                                .clientSecret(googleClientPw)
                                                                .code(new String(decodeByte))
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

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(user))
                .build();
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
                .profile(profile)
                .email(userInfo.getEmail())
                .nickname("GOOGLE" + uuid)
                .aboutMe("GOOGLE" + uuid+"님 안녕하세요 자신을 소개해 주세요!")
                .password(BCrypt.hashpw(uuid, BCrypt.gensalt()))
                .build();

        userRepository.saveAndFlush(user);
        userSearchRepository.save(UserDocument.ofEntity(user));

        return user;
    }
}

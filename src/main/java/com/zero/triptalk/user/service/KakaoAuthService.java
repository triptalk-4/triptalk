package com.zero.triptalk.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.triptalk.config.JwtService;
import com.zero.triptalk.exception.custom.UserException;
import com.zero.triptalk.user.entity.UserDocument;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.enumType.UserLoginRole;
import com.zero.triptalk.user.enumType.UserTypeRole;
import com.zero.triptalk.user.repository.UserRepository;
import com.zero.triptalk.user.repository.UserSearchRepository;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import static com.zero.triptalk.exception.code.UserErrorCode.KAKAO_NICKNAME_ERROR;

@Service
public class KakaoAuthService {
    private final UserRepository repository;
    private final UserSearchRepository userSearchRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Value("${kakao.redirect.url}")
    private String kakaoRedirectUrl;

    @Value("${kakao.password}")
    private String password;

    @Value("${cloud.aws.image}")
    private String profile;

    public KakaoAuthService(UserRepository repository, UserSearchRepository userSearchRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) throws IOException {
        this.repository = repository;
        this.userSearchRepository = userSearchRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public JsonNode getKakaoAccessToken(String code) {
        final String requestUrl = "https://kauth.kakao.com/oauth/token";

        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(requestUrl);

        try {
            String json = String.format(
                    "grant_type=authorization_code&client_id=%s&redirect_uri=%s&code=%s",
                    kakaoClientId,
                    kakaoRedirectUrl,
                    code
            );

            post.setEntity(new StringEntity(json, "UTF-8"));
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");

            HttpResponse response = client.execute(post);

            ObjectMapper mapper = new ObjectMapper();
            String responseBody = EntityUtils.toString(response.getEntity());
            return mapper.readTree(responseBody);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public JsonNode getKakaoUserInfo(String accessToken) {
        final String requestUrl = "https://kapi.kakao.com/v2/user/me";

        HttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(requestUrl);
        get.setHeader("Authorization", "Bearer " + accessToken);

        try {
            HttpResponse response = client.execute(get);
            int responseCode = response.getStatusLine().getStatusCode();

            ObjectMapper mapper = new ObjectMapper();
            String responseBody = EntityUtils.toString(response.getEntity());

            return mapper.readTree(responseBody);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

     public static String generateRandomNumber() {
            // Create an instance of the Random class
            Random random = new Random();

            // Generate a random 6-digit number
            int randomNumber = 100000 + random.nextInt(900000);

            // Convert the random number to a string

         return String.valueOf(randomNumber);
        }



    public String loginKakao(String nickname, String email) {
            String nicknameByRandom =  "kakao" + generateRandomNumber();


            Optional<UserEntity> existingUser = repository.findByEmail(email);
            // 회원일 때 로그인
            if (existingUser.isPresent()) {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                email,
                                password
                        )
                );
                var user = repository.findByEmail(email)
                        .orElseThrow();
                return jwtService.generateToken(user);
            }

            Optional<UserEntity> existingNickname = repository.findByNickname(nicknameByRandom);
            if (existingNickname.isPresent()) {
                throw new UserException(KAKAO_NICKNAME_ERROR);
            }
            LocalDateTime currentTime = LocalDateTime.now();


        String aboutMe = nicknameByRandom+"님 안녕하세요 자신을 소개해 주세요!";
      
            var user = UserEntity.builder()
                    .name(nickname)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .nickname(nicknameByRandom)
                    .UserType(UserTypeRole.USER)
                    .profile(profile)
                    .userLoginRole(UserLoginRole.KAKAO_USER_LOGIN)
                    .aboutMe(aboutMe)
                    .registerAt(currentTime)
                    .updateAt(currentTime)
                    .build();

            repository.save(user);
            userSearchRepository.save(UserDocument.ofEntity(user));

        return jwtService.generateToken(user);
    }

}

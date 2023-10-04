package com.zero.triptalk.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.triptalk.config.JwtService;
import com.zero.triptalk.exception.custom.UserException;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.enumType.UserLoginRole;
import com.zero.triptalk.user.enumType.UserTypeRole;
import com.zero.triptalk.user.repository.UserRepository;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.springframework.mail.javamail.JavaMailSender;
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
    private final LocalDateTime currentTime = LocalDateTime.now();

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender mailSender; // Spring MailSender

    public KakaoAuthService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, JavaMailSender mailSender) throws IOException {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.mailSender = mailSender;
    }

    public String generateKakaoAuthorizationUrl() {

        StringBuilder url = new StringBuilder();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id=" + "c5e7d3418384adeea868cb7470027a67");
        url.append("&redirect_uri=" + "http://localhost:8080/kakao/callback");
        url.append("&response_type=code");
        return url.toString();
    }

    public JsonNode getKakaoAccessToken(String code) {
        final String requestUrl = "https://kauth.kakao.com/oauth/token";

        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(requestUrl);

        try {
            String json = String.format(
                    "grant_type=authorization_code&client_id=%s&redirect_uri=%s&code=%s",
                    "c5e7d3418384adeea868cb7470027a67",
                    "http://localhost:8080/kakao/callback",
                    code
            );

            post.setEntity(new StringEntity(json, "UTF-8"));
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");

            HttpResponse response = client.execute(post);
            int responseCode = response.getStatusLine().getStatusCode();

            System.out.println("\nSending 'POST' request to URL : " + requestUrl);
            System.out.println("Post parameters : " + json);
            System.out.println("Response Code : " + responseCode);

            ObjectMapper mapper = new ObjectMapper();
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println("responseBody = " + responseBody);
            System.out.println("mapper.readTree(responseBody = " + mapper.readTree(responseBody));

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

            System.out.println("\nSending 'GET' request to URL : " + requestUrl);
            System.out.println("Response Code : " + responseCode);

            ObjectMapper mapper = new ObjectMapper();
            String responseBody = EntityUtils.toString(response.getEntity());

            System.out.println("responseBody = " + responseBody);

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
            String randomString = String.valueOf(randomNumber);

            return randomString;
        }



    public String loginKakao(String nickname, String email) {
            String password = "kakaoLogin1!";
            String nicknameByRandom =  nickname + generateRandomNumber();


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
                var jwtToken = jwtService.generateToken(user);
                return jwtToken;
            }

            Optional<UserEntity> existingNickname = repository.findByNickname(nicknameByRandom);
            if (existingNickname.isPresent()) {
                throw new UserException(KAKAO_NICKNAME_ERROR);
            }
            LocalDateTime currentTime = LocalDateTime.now();

            var user = UserEntity.builder()
                    .name(nickname)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .nickname(nicknameByRandom)
                    .UserType(UserTypeRole.USER)
                    .userLoginRole(UserLoginRole.KAKAO_USER_LOGIN)
                    .registerAt(currentTime)
                    .updateAt(currentTime)
                    .build();

            repository.save(user);

            var jwtToken = jwtService.generateToken(user);

            return jwtToken;
    }



}

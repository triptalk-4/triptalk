package com.zero.triptalk.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.request.*;
import com.zero.triptalk.user.response.*;
import com.zero.triptalk.user.service.AuthenticationService;
import com.zero.triptalk.user.service.KakaoAuthService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/api/users")
public class AuthenticationController {

    private final AuthenticationService service;

    private final KakaoAuthService kakaoAuthService;



    public AuthenticationController(AuthenticationService service, KakaoAuthService kakaoAuthService) {
        this.service = service;
        this.kakaoAuthService = kakaoAuthService;
    }

    /**
     * 일반 회원가입
     * @param request
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {

            AuthenticationResponse response = service.register(request);
            return ResponseEntity.ok(response);
    }

    @PostMapping("/register/email/send")
    public ResponseEntity<EmailCheckResponse> registerEmailSend(@RequestBody EmailCheckRequest request) throws MessagingException {

        EmailCheckResponse response = service.emailSend(request);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/register/email/check")
    public ResponseEntity<EmailCheckOkResponse> registerEmailCheckToken(@RequestBody EmailTokenRequest request) {

            EmailCheckOkResponse response = service.registerEmailCheckToken(request);
            return ResponseEntity.ok(response);
    }

    @PostMapping("/kakao/login")
    public KakaoResponse registerForKakao(@RequestBody KakaoRequest request)  {
        try {
            String code = request.getIngaCode();

            JsonNode access_token = kakaoAuthService.getKakaoAccessToken(code);

            ObjectMapper mapper = new ObjectMapper();

            JsonNode jsonNode = mapper.readTree(access_token.traverse());

            // 이메일을 위해 남겨진 것
            String accessToken = jsonNode.get("access_token").asText();

            // 로그인 을 위해 남겨진 코드 (관련한 내용 처리)
            JsonNode getUserInfo = kakaoAuthService.getKakaoUserInfo(accessToken);

            // Assuming you have the JSON response in a string variable called responseBody
            String responseBody = String.valueOf(getUserInfo);

            // Create an ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            // Parse the JSON response
            JsonNode jsonNodeByAccountInfo = objectMapper.readTree(responseBody);

            // Extract the "nickname" and "email" values
            String nickname = jsonNodeByAccountInfo.get("properties").get("nickname").asText();
            String email = jsonNodeByAccountInfo.get("kakao_account").get("email").asText();

            String jwtFinalToken  = kakaoAuthService.loginKakao(nickname,email);

            return KakaoResponse.builder()
                    .kakaoLoginOk("로그인이 완료되었습니다")
                    .token(jwtFinalToken)
                    .build();
        } catch (IOException e) {
            e.printStackTrace(); // 예외 스택 트레이스를 출력합니다.

            return KakaoResponse.builder()
                    .kakaoLoginOk("로그인에 실패했습니다: " + e.getMessage()) // 실패 메시지를 추가
                    .token("x") // 토큰은 비워서 반환
                    .build();
        }
    }


    @PostMapping("/update/password/check")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<PasswordCheckOkResponse> updateEmailCheckToken(@RequestBody EmailTokenRequest request) {

        PasswordCheckOkResponse response = service.PasswordCheckToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update/nickname/check")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<NicknameCheckOkResponse> nickname(@RequestBody NicknameCheckRequest request) {

        NicknameCheckOkResponse response = service.NicknameCheckToken(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 업데이트
     * @param request
     * @return
     */
    @PutMapping("/update/profile")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<AuthenticationResponse> updateRegister(@RequestBody UpdateRegisterRequest request) {

        AuthenticationResponse response = service.UpdateRegister(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<UserEntity> SeeMyProfile() {

        UserEntity response = service.SeeMyProfileRegister();
        return ResponseEntity.ok(response);
    }


    /**
     * 로그인
     * @param request
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/planners/byUser")
    public Page<MyPlannerBoardResponse> getPlannersByUser(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int pageSize) {
        UserEntity user = service.getUserByEmail(); // 해당 userId에 해당하는 유저 정보를 가져옵니다.
        if (user != null) {
            Pageable pageable = PageRequest.of(page, pageSize);
            return service.getPlannersByUser(user, pageable);
        } else {
            // 유저가 존재하지 않을 경우 예외 처리
            return Page.empty();
        }
    }

    @GetMapping("/planners/userSave")
    public Page<LikePlannerResponse> getPlannersUserLike(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int pageSize) {
        UserEntity user = service.getUserByEmail(); // 해당 userId에 해당하는 유저 정보를 가져옵니다.
        if (user != null) {
            Pageable pageable = PageRequest.of(page, pageSize);
            return service.getPlannersByUserLike(user, pageable);
        } else {
            // 유저가 존재하지 않을 경우 예외 처리
            return Page.empty();
        }
    }






}

package com.zero.triptalk.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.triptalk.user.request.KakaoRequest;
import com.zero.triptalk.user.response.KakaoResponse;
import com.zero.triptalk.user.service.KakaoAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/api/social")
public class KakaoController {

    private final KakaoAuthService service;

    public KakaoController(KakaoAuthService service) {
        this.service = service;
    }


    @GetMapping(value="/oauth")
    public String kakaoConnect() {

        String url = service.generateKakaoAuthorizationUrl();

        return "redirect:" + url;
    }

    @PostMapping("/login")
    public KakaoResponse registerForKakao(@RequestBody KakaoRequest request) throws IOException {
        try {
            String code = request.getToken();

            JsonNode access_token = service.getKakaoAccessToken(code);

            ObjectMapper mapper = new ObjectMapper();

            JsonNode jsonNode = mapper.readTree(access_token.traverse());

            // 이메일을 위해 남겨진 것
            String accessToken = jsonNode.get("access_token").asText();

            // 로그인 을 위해 남겨진 코드 (관련한 내용 처리)
            JsonNode getUserInfo = service.getKakaoUserInfo(accessToken);

            // Assuming you have the JSON response in a string variable called responseBody
            String responseBody = String.valueOf(getUserInfo);

            // Create an ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            // Parse the JSON response
            JsonNode jsonNodeByAccountInfo = objectMapper.readTree(responseBody);

            // Extract the "nickname" and "email" values
            String nickname = jsonNodeByAccountInfo.get("properties").get("nickname").asText();
            String email = jsonNodeByAccountInfo.get("kakao_account").get("email").asText();

            String jwtfinalToken  = service.loginKakao(nickname,email);

            System.out.println("jwtfinalToken = " + jwtfinalToken);

            return KakaoResponse.builder()
                    .kakaoLoginOk("로그인이 완료되었습니다")
                    .token(jwtfinalToken)
                    .build();
        } catch (IOException e) {
            // IOException이 발생한 경우 이곳으로 제어 흐름이 이동
            // 예외 처리 또는 오류 로깅을 수행할 수 있습니다.
            e.printStackTrace(); // 예외 스택 트레이스를 출력합니다.

            // 예외를 적절하게 처리하고 오류 응답을 반환합니다.
            // 예를 들어, 오류 메시지를 담은 응답 객체를 반환할 수 있습니다.
            return KakaoResponse.builder()
                    .kakaoLoginOk("로그인에 실패했습니다: " + e.getMessage()) // 실패 메시지를 추가
                    .token("") // 토큰은 비워서 반환
                    .build();
        }
    }


}

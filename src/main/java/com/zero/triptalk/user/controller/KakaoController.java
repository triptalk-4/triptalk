package com.zero.triptalk.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.triptalk.user.request.KakaoRequest;
import com.zero.triptalk.user.response.KakaoResponse;
import com.zero.triptalk.user.service.KakaoAuthService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/kakao")
public class KakaoController {



    private final KakaoAuthService service;

    public KakaoController(KakaoAuthService service) {
        this.service = service;
    }


    @GetMapping(value="/oauth")
    public String kakaoConnect() {

        String url = service.generateKakaoAuthorizationUrl();

        return "redirect:" + url.toString();
    }

    @PostMapping("/login")
    public KakaoResponse registerForKakao(@RequestBody KakaoRequest request) throws IOException {

        JsonNode access_token = service.getKakaoAccessToken(request.getToken());

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

        try {
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return KakaoResponse.builder()
                .kakaoLoginOk("로그인 실패")
                .token("로그인실패")
                .build();

    }

    @RequestMapping(value="/callback",produces="application/json",method= {RequestMethod.GET, RequestMethod.POST})
    public String kakaoLogin(@RequestParam("code")String code, RedirectAttributes ra, HttpSession session, HttpServletResponse response, Model model)throws IOException {

        System.out.println("kakao code:"+code);
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

        try {
            // Parse the JSON response
            JsonNode jsonNodeByAccountInfo = objectMapper.readTree(responseBody);

            // Extract the "nickname" and "email" values
            String nickname = jsonNodeByAccountInfo.get("properties").get("nickname").asText();
            String email = jsonNodeByAccountInfo.get("kakao_account").get("email").asText();

            String jwtfinalToken  = service.loginKakao(nickname,email);

            System.out.println("jwtfinalToken = " + jwtfinalToken);

            return jwtfinalToken;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return code;
    }

}

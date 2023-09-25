package com.zero.triptalk.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/auth/google")
public class GoogleLoginController {

    private final GoogleLoginService googleLoginService;

    @PostMapping
    public String getLoginUrl(){

        return googleLoginService.getLoginUrl();
    }

    @GetMapping
    public ResponseEntity<String> loginGoogle(@RequestParam(value = "code") String code){

        return ResponseEntity.ok(googleLoginService.doSocialLogin(code));
    }
}

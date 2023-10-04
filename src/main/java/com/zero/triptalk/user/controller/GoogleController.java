package com.zero.triptalk.user.controller;

import com.zero.triptalk.user.service.GoogleAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/auth/google")
public class GoogleController {

    private final GoogleAuthService googleAuthService;

    @PostMapping
    public String getLoginUrl(){

        return googleAuthService.getLoginUrl();
    }

    @GetMapping
    public ResponseEntity<String> loginGoogle(@RequestParam(value = "code") String code){

        return ResponseEntity.ok(googleAuthService.doSocialLogin(code));
    }
}

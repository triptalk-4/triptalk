package com.zero.triptalk.user.controller;

import com.zero.triptalk.user.response.AuthenticationResponse;
import com.zero.triptalk.user.service.GoogleAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/auth/google")
public class GoogleController {

    private final GoogleAuthService googleAuthService;

    @PostMapping
    public ResponseEntity<String> getLoginUrl(){

        return ResponseEntity.ok(googleAuthService.getLoginUrl());
    }

    @GetMapping
    public ResponseEntity<AuthenticationResponse> loginGoogle(@RequestParam(value = "code") String code){

        log.debug("controller :::::::::::::::" + code);

        return ResponseEntity.ok(googleAuthService.doSocialLogin(code));
    }
}

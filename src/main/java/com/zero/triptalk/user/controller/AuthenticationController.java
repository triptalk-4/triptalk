package com.zero.triptalk.user.controller;

import com.zero.triptalk.exception.custom.UserException;
import com.zero.triptalk.user.request.AuthenticationRequest;
import com.zero.triptalk.user.request.EmailCheckRequest;
import com.zero.triptalk.user.request.EmailTokenRequest;
import com.zero.triptalk.user.request.RegisterRequest;
import com.zero.triptalk.user.response.AuthenticationResponse;
import com.zero.triptalk.user.response.EmailCheckOkResponse;
import com.zero.triptalk.user.response.EmailCheckResponse;
import com.zero.triptalk.user.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class AuthenticationController {

    private final AuthenticationService service;



    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    /**
     * 일반 회원가입
     * @param request
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthenticationResponse response = service.register(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/register/email/send")
    public ResponseEntity<EmailCheckResponse> registerEmailSend(@RequestBody EmailCheckRequest request) {
        try {
            EmailCheckResponse response = service.emailSend(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new EmailCheckResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new EmailCheckResponse("서버 오류가 발생했습니다."));
        }
    }

    @PostMapping("/register/email/check")
    public ResponseEntity<?> registerEmailCheckToken(@RequestBody EmailTokenRequest request) {
        try {
            EmailCheckOkResponse response = service.registerEmailCheckToken(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
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
        return ResponseEntity.ok((AuthenticationResponse) service.authenticate(request));
    }






}

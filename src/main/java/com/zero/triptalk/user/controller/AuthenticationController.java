package com.zero.triptalk.user.controller;

import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.request.*;
import com.zero.triptalk.user.response.*;
import com.zero.triptalk.user.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.util.List;

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

            AuthenticationResponse response = service.register(request);
            return ResponseEntity.ok(response);
    }

    @PostMapping("/register/email/send")
    public ResponseEntity<EmailCheckResponse> registerEmailSend(@RequestBody EmailCheckRequest request) throws MessagingException {

        EmailCheckResponse response = service.emailSend(request);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/register/email/check")
    public ResponseEntity<?> registerEmailCheckToken(@RequestBody EmailTokenRequest request) {

            EmailCheckOkResponse response = service.registerEmailCheckToken(request);
            return ResponseEntity.ok(response);
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
     * @param files
     * @return
     */
    @PutMapping("/update/profile")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<AuthenticationResponse> updateRegister(@RequestPart UpdateRegisterRequest request,
                                            @RequestPart("files") List<MultipartFile> files) {

            AuthenticationResponse response = service.UpdateRegister(request, files);
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






}

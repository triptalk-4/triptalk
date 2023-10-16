package com.zero.triptalk.user.controller;

import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.request.*;
import com.zero.triptalk.user.response.*;
import com.zero.triptalk.user.service.AuthenticationService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/planners/userLike")
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

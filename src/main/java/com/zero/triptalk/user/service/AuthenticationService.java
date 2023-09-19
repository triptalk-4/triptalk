package com.zero.triptalk.user.service;

import com.zero.triptalk.config.JwtService;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.enumType.UserTypeRole;
import com.zero.triptalk.user.repository.UserRepository;
import com.zero.triptalk.user.request.AuthenticationRequest;
import com.zero.triptalk.user.request.RegisterRequest;
import com.zero.triptalk.user.response.AuthenticationResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final LocalDateTime currentTime = LocalDateTime.now();

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();
        String nickname = request.getNickname();

        // 이메일 유효성 확인
        if (!UserEntity.isValidEmail(email)) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
        }

        // 비밀번호 유효성 확인
        if (!UserEntity.isValidPassword(password)) {
            throw new IllegalArgumentException("비밀번호는 특수문자를 포함한 8글자 이상이어야 합니다.");
        }

        // 이미 존재하는 이메일인지 확인
        Optional<UserEntity> existingUser = repository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        Optional<UserEntity> existingNickname = repository.findByNickname(nickname);
        if (existingNickname.isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        var user = UserEntity.builder()
                .name(request.getName())
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .UserType(UserTypeRole.USER)
                .registerAt(currentTime)
                .updateAt(currentTime)
                .build();

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public Object authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


}

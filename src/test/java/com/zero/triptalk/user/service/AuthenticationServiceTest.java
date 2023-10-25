package com.zero.triptalk.user.service;

import com.zero.triptalk.config.JwtService;
import com.zero.triptalk.exception.custom.UserException;
import com.zero.triptalk.user.entity.UserDocument;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.repository.UserRepository;
import com.zero.triptalk.user.repository.UserSearchRepository;
import com.zero.triptalk.user.request.AuthenticationRequest;
import com.zero.triptalk.user.request.EmailTokenRequest;
import com.zero.triptalk.user.request.NicknameCheckRequest;
import com.zero.triptalk.user.request.RegisterRequest;
import com.zero.triptalk.user.response.AuthenticationResponse;
import com.zero.triptalk.user.response.NicknameCheckOkResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSearchRepository userSearchRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Value("${cloud.aws.image}")
    private String imagePropertyValue;

    @BeforeEach
    public void setUp() {
        // Setup any necessary mock behaviors or initializations
    }


    @Test
    @DisplayName("회원가입 테스트 ")
    public void testRegisterValidUser() {
        // Arrange
        RegisterRequest request = new RegisterRequest("테스트", "gegdfbcx", "isnot@example.com", "password1 !");
        // 이메일 찾기 (중복 데이터 안넣기 위함 )
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        // 닉네임 찾기 (중복 데이터 안되기 위함 )
        when(userRepository.findByNickname(request.getNickname())).thenReturn(Optional.empty());

        // Act
        AuthenticationResponse response = authenticationService.register(request);

        // Assert
        assertNotNull(response);
        // 회원가입이 완료 되었을 때
        assertEquals("회원가입이 완료되었습니다. 로그인 해주세요 감사합니다!", response.getRegisterOk());
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(userSearchRepository, times(1)).save(any(UserDocument.class));
    }

    @Test
    @DisplayName("이메일 생성")
    public void testAuthenticateValidUser() {
        // Arrange
        // request 에 값넣기
        AuthenticationRequest request = new AuthenticationRequest("testmail@test.com", "password");
        UserEntity user = new UserEntity();
        // 이메일 찾기
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(jwtService.generateToken(user)).thenReturn("토큰생성값");

        // Act
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Assert
        assertNotNull(response);
        assertEquals("토큰생성값", response.getToken());
    }

    @Test
    @DisplayName("새로운 닉넴 처리 -> 닉네임 중복값 없을때의 처리")
    public void testNicknameCheckTokenAvailable() {
        // Arrange
        NicknameCheckRequest request = new NicknameCheckRequest("새로운닉넴");
        when(userRepository.findByNickname(request.getNickname()))
                .thenReturn(Optional.empty());

        // Act
        NicknameCheckOkResponse response =
                authenticationService.NicknameCheckToken(request);

        // Assert
        assertNotNull(response);
        assertEquals("해당 닉네임(새로운닉넴)은 사용이 가능합니다",
                response.getNicknameCheckOkOrNotOk());
    }

    @Test
    @DisplayName("새로운 닉넴 처리 -> 닉네임 중복값 있을 때의 처리")
    public void testNicknameCheckTokenUnavailable() {
        // Arrange
        NicknameCheckRequest request = new NicknameCheckRequest("새로운닉넴");
        UserEntity existingUser = new UserEntity();
        when(userRepository.findByNickname(request.getNickname())).thenReturn(Optional.of(existingUser));

        // Act
        NicknameCheckOkResponse response = authenticationService.NicknameCheckToken(request);

        // Assert
        assertNotNull(response);
        assertEquals("해당 닉네임(새로운닉넴)은 이미 다른 사용자가 사용하고 있습니다. 다른 닉네임을 설정해 주세요"
                           , response.getNicknameCheckOkOrNotOk());
    }

    @Test
    @DisplayName("비밀번호 채크 후 틀리면 안되게 설정")
    public void testPasswordCheckTokenInvalidPassword() {
        // Arrange
        EmailTokenRequest request =
                new EmailTokenRequest("token","validEmail@example.com", "invalidPassword");
        UserEntity existingUser = new UserEntity();
        // 틀린 패스워드
        existingUser.setPassword(passwordEncoder.encode("validPassword")); // Set the encoded password

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(request.getPassword(), existingUser.getPassword())).thenReturn(false);

        // Act and Assert
        assertThrows(UserException.class, () -> authenticationService.PasswordCheckToken(request));
    }


}

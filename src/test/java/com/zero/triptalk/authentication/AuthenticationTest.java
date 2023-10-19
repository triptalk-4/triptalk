//package com.zero.triptalk.authentication;
//
//import com.zero.triptalk.config.JwtService;
//import com.zero.triptalk.exception.custom.UserException;
//import com.zero.triptalk.user.entity.UserEntity;
//import com.zero.triptalk.user.repository.UserRepository;
//import com.zero.triptalk.user.request.RegisterRequest;
//import com.zero.triptalk.user.service.AuthenticationService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//
//import static com.zero.triptalk.exception.code.UserErrorCode.EMAIL_APPROVAL_DENIED;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//
//public class AuthenticationTest {
//    @InjectMocks
//    private AuthenticationService service;
//
//    @Mock
//    private UserRepository repository;
//
//    @Mock
//    private JwtService jwtService;
//
//    @Test
//    @DisplayName("이메일 형식 확인 - mail@mail.com")
//    public void register() {
//
//        RegisterRequest request = new RegisterRequest();
//        request.setEmail("123"); // 형식에 안맞음
//
//        // Assert that a UserException is thrown when registering
//        assertThrows(UserException.class, () -> {
//            if (!UserEntity.isValidEmail(request.getEmail())) {
//                throw new UserException(EMAIL_APPROVAL_DENIED);
//            }
//        });
//    }
//}

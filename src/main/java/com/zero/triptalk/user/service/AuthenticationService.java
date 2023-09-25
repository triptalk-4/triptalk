package com.zero.triptalk.user.service;

import com.zero.triptalk.config.JwtService;
import com.zero.triptalk.exception.type.UserException;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.enumType.UserLoginRole;
import com.zero.triptalk.user.enumType.UserTypeRole;
import com.zero.triptalk.user.repository.UserRepository;
import com.zero.triptalk.user.request.AuthenticationRequest;
import com.zero.triptalk.user.request.EmailTokenRequest;
import com.zero.triptalk.user.request.RegisterRequest;
import com.zero.triptalk.user.response.AuthenticationResponse;
import com.zero.triptalk.user.response.EmailCheckResponse;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.zero.triptalk.exception.code.UserErrorCode.*;

@Service
public class AuthenticationService {

    private final LocalDateTime currentTime = LocalDateTime.now();

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender mailSender; // Spring MailSender

    private static  String senderMail= "juhun104@naver.com";
    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, JavaMailSender mailSender) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.mailSender = mailSender;
    }

    static int createNumber(){
      int number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
      return number;
    }

    public int sendMail(String toEmail) throws MessagingException {
        int number = createNumber();
        System.out.println("number = " + number);
        MimeMessage m = mailSender.createMimeMessage();
        MimeMessageHelper h = new MimeMessageHelper(m, "UTF-8");
        h.setFrom(senderMail); // 이메일 발신자 설정
        h.setTo(toEmail); // 받는 이메일 주소 설정
        h.setSubject("제목"); // 이메일 제목 설정
        h.setText("인증코드: " + number); // 이메일 내용 설정
        mailSender.send(m);
        return number; // 성공 상태 코드를 반환 (수정 필요)
    }


    public AuthenticationResponse register(RegisterRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();
        String nickname = request.getNickname();

        // 이메일 유효성 확인
        if (!UserEntity.isValidEmail(email)) {
            throw new UserException(EMAIL_APPROVAL_DENIED);
        }

        // 비밀번호 유효성 확인
        if (!UserEntity.isValidPassword(password)) {
            throw new UserException(PASSWORD_APPROVAL_DENIED);
        }

        // 이미 존재하는 이메일인지 확인
        Optional<UserEntity> existingUser = repository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new UserException(EMAIL_ALREADY_EXIST);
        }

        Optional<UserEntity> existingNickname = repository.findByNickname(nickname);
        if (existingNickname.isPresent()) {
            throw new UserException(NICKNAME_ALREADY_EXIST);
        }

        var user = UserEntity.builder()
                .name(request.getName())
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .UserType(UserTypeRole.USER)
                .userLoginRole(UserLoginRole.GENERAL_USER_LOGIN)
                .registerAt(currentTime)
                .updateAt(currentTime)
                .build();

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .registerOk("회원가입이 완료되었습니다. 로그인 해주세요 감사합니다!")
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


    public EmailCheckResponse emailCheck(EmailTokenRequest request) throws MessagingException {
        String email = request.getEmail();
        int token = sendMail(email); // createMail 메서드에서 토큰 생성 및 이메일 전송(email);

        return EmailCheckResponse.builder()
                .PostMailOk("이메일이 전송 완료되었습니다.")
                .emailToken(token)
                .build();
    }
}

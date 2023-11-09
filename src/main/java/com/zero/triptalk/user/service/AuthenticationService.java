package com.zero.triptalk.user.service;

import com.zero.triptalk.alert.dto.response.AlertCntResponse;
import com.zero.triptalk.component.RedisUtil;
import com.zero.triptalk.config.JwtService;
import com.zero.triptalk.exception.code.UserErrorCode;
import com.zero.triptalk.exception.custom.UserException;
import com.zero.triptalk.image.service.ImageService;
import com.zero.triptalk.like.repository.UserSaveRepository;
import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.planner.repository.PlannerRepository;
import com.zero.triptalk.user.entity.UserDocument;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.enumType.UserLoginRole;
import com.zero.triptalk.user.enumType.UserTypeRole;
import com.zero.triptalk.user.repository.UserRepository;
import com.zero.triptalk.user.repository.UserSearchRepository;
import com.zero.triptalk.user.request.*;
import com.zero.triptalk.user.response.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.zero.triptalk.exception.code.UserErrorCode.*;

@Slf4j
@Service
public class AuthenticationService {

    private LocalDateTime currentTime = LocalDateTime.now();
    private final UserRepository repository;
    private final PlannerRepository plannerRepository;
    private final UserSaveRepository userSaveRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender mailSender;
    private final ImageService imageService;
    private final RedisUtil redisUtil;
    private final UserSearchRepository userSearchRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.image}")
    private String profile;

    @Value("${spring.mail.username}")
    private String senderMail;

    public AuthenticationService(UserRepository repository, PlannerRepository plannerRepository, UserSaveRepository userSaveRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, JavaMailSender mailSender, ImageService imageService, RedisUtil redisUtil, UserSearchRepository userSearchRepository) {
        this.repository = repository;
        this.plannerRepository = plannerRepository;
        this.userSaveRepository = userSaveRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.mailSender = mailSender;
        this.imageService = imageService;
        this.redisUtil = redisUtil;
        this.userSearchRepository = userSearchRepository;
    }

    public String userEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = "기본 이메일";
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            email = userDetails.getUsername(); // 사용자 이메일 정보를 추출
        }

        return email;
    }

    public static String createRandomString() {

        return RandomStringUtils.randomAlphanumeric(6);
    }


    public String sendMail(String toEmail) throws MessagingException {
        String number = createRandomString();
        MimeMessage m = mailSender.createMimeMessage();
        MimeMessageHelper h = new MimeMessageHelper(m, "UTF-8");
        h.setFrom(senderMail);
        h.setTo(toEmail);
        h.setSubject("안녕하세요 여행하는 즐거움 triptalk 입니다!");
        h.setText("인증코드: " + number + "입니다 감사합니다!");
        mailSender.send(m);
        return number; // 성공 상태 코드를 반환 (수정 필요)
    }


    public AuthenticationResponse register(RegisterRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();
        String nickname = request.getNickname();

        if (!UserEntity.isValidEmail(email)) {
            throw new UserException(EMAIL_APPROVAL_DENIED);
        }

        if (!UserEntity.isValidPassword(password)) {
            throw new UserException(PASSWORD_APPROVAL_DENIED);
        }

        Optional<UserEntity> existingUser = repository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new UserException(EMAIL_ALREADY_EXIST);
        }

        Optional<UserEntity> existingNickname = repository.findByNickname(nickname);
        if (existingNickname.isPresent()) {
            throw new UserException(NICKNAME_ALREADY_EXIST);
        }

        String aboutMe = nickname+"님 안녕하세요 자신을 소개해 주세요!";

        var user = UserEntity.builder()
                .name(request.getName())
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .UserType(UserTypeRole.USER)
                .userLoginRole(UserLoginRole.GENERAL_USER_LOGIN)
                .registerAt(currentTime)
                .updateAt(currentTime)
                .profile(profile)
                .aboutMe(aboutMe)
                .build();

        repository.save(user);
        userSearchRepository.save(UserDocument.ofEntity(user));

        return AuthenticationResponse.builder()
                .registerOk("회원가입이 완료되었습니다. 로그인 해주세요 감사합니다!")
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new UserException(NO_VAILD_EMAIL_AND_PASSWORD);
        }
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    public EmailCheckResponse emailSend(EmailCheckRequest request) throws MessagingException {
        String email = request.getEmail();

        Optional<UserEntity> existingUser = repository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new UserException(EMAIL_ALREADY_EXIST);
        }

        String token = sendMail(email); // createMail 메서드에서 토큰 생성 및 이메일 전송(email);

        redisUtil.setDataExpire(String.valueOf(token),email,60*5L);

        return EmailCheckResponse.builder()
                .emailToken("이메일을 확인하여 주세요")
                .PostMailOk("이메일이 전송 완료되었습니다. 이메일 인증 유효 시간은 5분입니다!")
                .build();
    }

    public EmailCheckOkResponse registerEmailCheckToken(EmailTokenRequest request) {
        String emailSendToken = request.getToken();
        String storedToken = redisUtil.getData(emailSendToken);

        if (storedToken != null && !storedToken.isEmpty()) {
               return EmailCheckOkResponse.builder()
                       .emailVerificationCompleted("이메일 인증이 완료 되었습니다. ")
                       .build();
        }

        return EmailCheckOkResponse.builder()
                .emailVerificationFailed("이메일 인증에 실패하였습니다")
                .build();
    }

    public String S3FileSaveAndOldImageDeleteAndNewProfile(String files, String oldImage) {

            if(!(profile.equals(oldImage))) {
                imageService.deleteFile(oldImage);
            }
        return files;
    }

    public AuthenticationResponse UpdateRegister(UpdateRegisterRequest request) {

        String email = request.getEmail();
        String newPassword = request.getNewPassword();
        String newNickname = request.getNewNickname();
        String newAboutMe = request.getNewAboutMe();
        String oldImage = request.getOldImage();
        String newImage = request.getNewImage();

        UserEntity existingUser = repository.findByEmail(email).orElseThrow(() ->
                                                new UserException(EMAIL_NOT_FOUND_ERROR));

        String newProfile = S3FileSaveAndOldImageDeleteAndNewProfile(newImage, oldImage);

        // 최종 업데이트 코드
        if (existingUser.getUserLoginRole().equals(UserLoginRole.KAKAO_USER_LOGIN)
                || existingUser.getUserLoginRole().equals(UserLoginRole.GOOGLE_USER_LOGIN)){

            existingUser.setUpdateAt(LocalDateTime.now());
            existingUser.setNickname(newNickname);
            existingUser.setAboutMe(newAboutMe);
            existingUser.setProfile(newProfile);

        } else {

            // 새로운 비밀번호가 제공된 경우 비밀번호 업데이트
            if (newPassword != null && !newPassword.isEmpty()) {
                String encodedNewPassword = passwordEncoder.encode(newPassword);
                existingUser.setPassword(encodedNewPassword);
            }

            existingUser.setUpdateAt(LocalDateTime.now());
            existingUser.setNickname(newNickname);
            existingUser.setAboutMe(newAboutMe);
            existingUser.setProfile(newProfile);
        }

        repository.save(existingUser);
        userSearchRepository.save(UserDocument.ofEntity(existingUser));

        return AuthenticationResponse.builder()
                                    .updateOk("업데이트가 완료되었습니다.")
                                    .token(jwtService.generateToken(existingUser))
                                    .build();
    }

    public PasswordCheckOkResponse PasswordCheckToken(EmailTokenRequest request) {
        Optional<UserEntity> existingUserOptional = repository.findByEmail(request.getEmail());

        if (existingUserOptional.isEmpty()) {
            throw new UserException(EMAIL_NOT_FOUND_ERROR);
        }

        UserEntity existingUser = existingUserOptional.get();
        String storedPasswordHash = existingUser.getPassword();

        if (!passwordEncoder.matches(request.getPassword(), storedPasswordHash)) {
            throw new UserException(UserErrorCode.PASSWORD_NOT_SAME);
        }
        return PasswordCheckOkResponse.builder()
                .passwordCheckOk("패스워드 체크가 완료되었습니다.")
                .build();
    }

    public UserEntity SeeMyProfileRegister() {
        String email =  userEmail();

        Optional<UserEntity> existingUserOptional = repository.findByEmail(email);

        if (existingUserOptional.isEmpty()) {
            throw new UserException(EMAIL_NOT_FOUND_ERROR);
        }
        return existingUserOptional.get();

    }

    public NicknameCheckOkResponse NicknameCheckToken(NicknameCheckRequest request) {

        Optional<UserEntity> existingUserOptional = repository.findByNickname(request.getNickname());

        if(request.getNickname().equals("")){
            return NicknameCheckOkResponse.builder()
                    .nicknameCheckOkOrNotOk("해당 닉네임에 값을 넣어 주세요")
                    .build();

        }

        if (existingUserOptional.isEmpty()) {
            return NicknameCheckOkResponse.builder()
                    .nicknameCheckOkOrNotOk("해당 닉네임("+request.getNickname()+")은 사용이 가능합니다")
                    .build();
        }else {
            return NicknameCheckOkResponse.builder()
                    .nicknameCheckOkOrNotOk("해당 닉네임(" + request.getNickname() + ")은 이미 다른 사용자가 사용하고 있습니다. 다른 닉네임을 설정해 주세요")
                    .build();
        }
    }

    public UserEntity getUserByEmail() {
        String userEmail = userEmail();

        return repository.findByEmail(userEmail).orElseThrow(() ->
                                        new UserException(USER_NOT_FOUND));

    }

    public Page<MyPlannerBoardResponse> getPlannersByUser(UserEntity user, Pageable pageable) {
        Page<Object[]> plannersPage = plannerRepository.findPlannersWithLikeCount(user, pageable);

        List<MyPlannerBoardResponse> myPlannerBoardResponses = plannersPage
                .stream()
                .map(data -> {
                    Planner planner = (Planner) data[0];
                    Object data1 = data[1];

                    MyPlannerBoardResponse response = new MyPlannerBoardResponse();
                    response.setPlannerId(planner.getPlannerId());
                    response.setTitle(planner.getTitle());
                    response.setThumbnail(planner.getThumbnail());
                    response.setViews(planner.getViews());
                    response.setCreateAt(planner.getCreatedAt().toString());

                    // LikeCount 설정
                    if (data1 instanceof Long) {
                        response.setLikeCount((Long) data1);
                    } else {
                        response.setLikeCount(0L);
                    }

                    return response;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(myPlannerBoardResponses, pageable, plannersPage.getTotalElements());
    }
    public Page<LikePlannerResponse> getPlannersByUserLike(UserEntity user, Pageable pageable) {
        Page<Object[]> plannersPage = userSaveRepository.findPlannersLikedByUserWithLikeCount(user, pageable);

        List<LikePlannerResponse> likePlannerResponses = plannersPage
                .stream()
                .map(data -> {
                    Planner planner = (Planner) data[0];
                    Long likeCount = (Long) data[1];

                    LikePlannerResponse response = new LikePlannerResponse();
                    response.setPlannerId(planner.getPlannerId());
                    response.setTitle(planner.getTitle());
                    response.setThumbnail(planner.getThumbnail());
                    response.setViews(planner.getViews());
                    response.setCreateAt(planner.getCreatedAt().toString());
                    response.setLikeCount(likeCount);

                    return response;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(likePlannerResponses, pageable, plannersPage.getTotalElements());
    }


}

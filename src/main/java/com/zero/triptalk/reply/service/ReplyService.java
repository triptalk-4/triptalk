package com.zero.triptalk.reply.service;

import com.zero.triptalk.exception.code.ReplyErrorCode;
import com.zero.triptalk.exception.code.UserErrorCode;
import com.zero.triptalk.exception.custom.ReplyException;
import com.zero.triptalk.exception.custom.UserException;
import com.zero.triptalk.planner.entity.PlannerDetail;
import com.zero.triptalk.planner.repository.PlannerDetailRepository;
import com.zero.triptalk.reply.dto.request.ReplyRequest;
import com.zero.triptalk.reply.dto.response.ReplyResponse;
import com.zero.triptalk.reply.entity.ReplyEntity;
import com.zero.triptalk.reply.repository.ReplyRepository;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReplyService {

    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final PlannerDetailRepository plannerDetailRepository;
    private static  String email = "";


    public String userEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            email = userDetails.getUsername(); // 사용자 이메일 정보를 추출
        }

        return email;
    }

    public ReplyResponse replyOk(Long plannerDetailId,ReplyRequest request) {

        PlannerDetail plannerDetail = plannerDetailRepository.findById(plannerDetailId)
                .orElseThrow(() -> new ReplyException(ReplyErrorCode.NO_Planner_Detail_Board));

        // 접속자 유저 찾기 -> 어떤 유저가 댓글을 달았는지 등록할 수 있다
        String email = userEmail(); // 이메일 불러오기
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.EMAIL_NOT_FOUND_ERROR));
        LocalDateTime currentTime = LocalDateTime.now();

        ReplyEntity reply = ReplyEntity.builder()
                .plannerDetail(plannerDetail)
                .user(userEntity)
                .reply(request.getReply())
                .createdAt(currentTime)
                .modifiedAt(currentTime)
                .build();

        replyRepository.save(reply);

        return ReplyResponse.builder()
                .postOk("댓글 등록이 완료 되었습니다!")
                .build();
    }

    public ReplyResponse replyUpdateOk(Long replyId,ReplyRequest request) {
        LocalDateTime currentTime = LocalDateTime.now();

        ReplyEntity reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyException(ReplyErrorCode.NO_Planner_Detail_Reply_Board));


        // 좋아요를 누른 접속자 유저 찾기
        String email = userEmail(); // 이메일 불러오기
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.EMAIL_NOT_FOUND_ERROR));
        // 쓴 유저가 아닐때 에러
        if(!(userEntity.equals(reply.getUser()))){
            throw new ReplyException(ReplyErrorCode.NO_Reply_Owner);
        }

        reply.setReply(request.getReply());
        reply.setModifiedAt(currentTime);

        replyRepository.save(reply);

        return ReplyResponse.builder()
                .postOk("댓글 업데이트가 완료 되었습니다.")
                .build();
    }

    public ReplyResponse replyDeleteOk(Long replyId) {

        ReplyEntity reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyException(ReplyErrorCode.NO_Planner_Detail_Reply_Board));


        // 좋아요를 누른 접속자 유저 찾기
        String email = userEmail(); // 이메일 불러오기
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.EMAIL_NOT_FOUND_ERROR));
        // 쓴 유저가 아닐때 에러
        if(!(userEntity.equals(reply.getUser()))){
            throw new ReplyException(ReplyErrorCode.NO_Reply_Owner);
        }
        replyRepository.delete(reply);

        return ReplyResponse.builder()
                .postOk("댓글 삭제가 완료 되었습니다.")
                .build();
    }
}

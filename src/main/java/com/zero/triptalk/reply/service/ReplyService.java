package com.zero.triptalk.reply.service;

import com.zero.triptalk.exception.code.ReplyErrorCode;
import com.zero.triptalk.exception.code.UserErrorCode;
import com.zero.triptalk.exception.custom.ReplyException;
import com.zero.triptalk.exception.custom.UserException;
import com.zero.triptalk.planner.entity.PlannerDetail;
import com.zero.triptalk.planner.repository.PlannerDetailRepository;
import com.zero.triptalk.reply.dto.request.ReplyRequest;
import com.zero.triptalk.reply.dto.response.ReplyGetResponse;
import com.zero.triptalk.reply.dto.response.ReplyResponse;
import com.zero.triptalk.reply.entity.ReplyEntity;
import com.zero.triptalk.reply.repository.ReplyRepository;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class ReplyService {

    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final PlannerDetailRepository plannerDetailRepository;

    public ReplyResponse replyOk(Long plannerDetailId, ReplyRequest request, String email) {


        PlannerDetail plannerDetail = plannerDetailRepository.findById(plannerDetailId)
                .orElseThrow(() -> new ReplyException(ReplyErrorCode.NO_PLANNER_DETAIL_BOARD));

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.EMAIL_NOT_FOUND_ERROR));

        ReplyEntity reply = ReplyEntity.builder()
                                        .plannerDetail(plannerDetail)
                                        .user(userEntity)
                                        .reply(request.getReply())
                                        .build();

        replyRepository.save(reply);

        return ReplyResponse.builder()
                .postOk("댓글 등록이 완료 되었습니다!")
                .build();
    }

    public ReplyResponse replyUpdateOk(Long replyId, ReplyRequest request, String email) {

        ReplyEntity reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyException(ReplyErrorCode.NO_PLANNER_DETAIL_REPLY_BOARD));

        // 좋아요를 누른 접속자 유저 찾기
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.EMAIL_NOT_FOUND_ERROR));
        // 쓴 유저가 아닐때 에러
        if (!(userEntity.equals(reply.getUser()))) {
            throw new ReplyException(ReplyErrorCode.NO_REPLY_OWNER);
        }

        reply.setReply(request.getReply());
        replyRepository.save(reply);

        return ReplyResponse.builder()
                .postOk("댓글 업데이트가 완료 되었습니다.")
                .build();
    }

    public ReplyResponse replyDeleteOk(Long replyId, String email) {

        ReplyEntity reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ReplyException(ReplyErrorCode.NO_PLANNER_DETAIL_REPLY_BOARD));

        // 좋아요를 누른 접속자 유저 찾기
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.EMAIL_NOT_FOUND_ERROR));
        // 쓴 유저가 아닐때 에러
        if (!(userEntity.equals(reply.getUser()))) {
            throw new ReplyException(ReplyErrorCode.NO_REPLY_OWNER);
        }
        replyRepository.delete(reply);

        return ReplyResponse.builder()
                .postOk("댓글 삭제가 완료 되었습니다.")
                .build();
    }

    public List<ReplyGetResponse> getRepliesByPlannerDetailId(Long plannerDetailId) {
        // PlannerDetail 번호로 PlannerDetail 조회
        PlannerDetail plannerDetail = plannerDetailRepository.findById(plannerDetailId)
                .orElseThrow(() -> new ReplyException(ReplyErrorCode.NO_Planner_Detail_Board));

        // 조회한 PlannerDetail에 속하는 댓글 목록 조회
        List<ReplyEntity> replies = replyRepository.findByPlannerDetail(plannerDetail);

        // ReplyEntity를 ReplyGetResponse로 매핑하고 최신순으로 정렬
        return replies.stream()
                        .map(ReplyGetResponse::ofEntity)
                        .sorted(Comparator.comparing(ReplyGetResponse::getCreateDt).reversed()) // 최신순으로 정렬
                        .collect(Collectors.toList());
    }

    public void deleteAllByPlannerDetail(PlannerDetail plannerDetail){
        replyRepository.deleteAllByPlannerDetail(plannerDetail);
    }
}

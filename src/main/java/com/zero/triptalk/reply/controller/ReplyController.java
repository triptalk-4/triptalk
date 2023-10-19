package com.zero.triptalk.reply.controller;

import com.zero.triptalk.reply.dto.request.ReplyRequest;
import com.zero.triptalk.reply.dto.response.ReplyGetResponse;
import com.zero.triptalk.reply.dto.response.ReplyResponse;
import com.zero.triptalk.reply.entity.ReplyEntity;
import com.zero.triptalk.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans")
public class ReplyController {

    private final ReplyService replyService;


    @GetMapping("/detail/replies/{plannerDetailId}")
    @PreAuthorize("hasAuthority('USER')")
    public List<ReplyGetResponse> getRepliesByPlannerDetail(@PathVariable Long plannerDetailId) {
        // replyService를 사용하여 PlannerDetail 번호로 댓글을 조회
        List<ReplyGetResponse> replyEntities = replyService.getRepliesByPlannerDetailId(plannerDetailId);

        return replyEntities;
    }


    /**
     * 댓글 하나 등록
     * @param plannerDetailId
     * @return
     */
    @PostMapping("/detail/{plannerDetailId}/reply")
    @PreAuthorize("hasAuthority('USER')")
    public ReplyResponse ReplyOk(@PathVariable Long plannerDetailId,
                                 @RequestBody ReplyRequest request) {

        // 댓글 달기
        ReplyResponse response = replyService.replyOk(plannerDetailId,request);

        return response;
    }

    @PutMapping("/detail/reply/{replyId}/update")
    public ReplyResponse ReplyUpdateOk(@PathVariable Long replyId,
                                          @RequestBody ReplyRequest request) {

        // 댓글 달기
        ReplyResponse response = replyService.replyUpdateOk(replyId,request);

        return response;
    }

    @DeleteMapping("/detail/reply/{replyId}/delete")
    public ReplyResponse ReplyDeleteOk(@PathVariable Long replyId) {

        // 댓글 달기
        ReplyResponse response = replyService.replyDeleteOk(replyId);

        return response;
    }




}

package com.zero.triptalk.reply.controller;

import com.zero.triptalk.reply.dto.request.ReplyRequest;
import com.zero.triptalk.reply.dto.response.ReplyResponse;
import com.zero.triptalk.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans")
public class ReplyController {

    private final ReplyService replyService;

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

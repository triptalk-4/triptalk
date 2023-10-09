package com.zero.triptalk.reply.controller;

import com.zero.triptalk.reply.dto.request.ReplyRequest;
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
    public ResponseEntity<Object> ReplyOk(@PathVariable Long plannerDetailId,
                                          @RequestBody ReplyRequest request) {

        // 댓글 달기
        Object response = replyService.ReplyOk(plannerDetailId,request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/detail/reply/{replyId}/update")
    public ResponseEntity<Object> ReplyUpdateOk(@PathVariable Long replyId,
                                          @RequestBody ReplyRequest request) {

        // 댓글 달기
        Object response = replyService.ReplyUpdateOk(replyId,request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/detail/reply/{replyId}/delete")
    public ResponseEntity<Object> ReplyDeleteOk(@PathVariable Long replyId) {

        // 댓글 달기
        Object response = replyService.ReplyDeleteOk(replyId);

        return ResponseEntity.ok(response);
    }




}

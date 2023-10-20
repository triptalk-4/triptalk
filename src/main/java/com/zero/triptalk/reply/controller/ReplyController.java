package com.zero.triptalk.reply.controller;

import com.zero.triptalk.reply.dto.request.ReplyRequest;
import com.zero.triptalk.reply.dto.response.ReplyGetResponse;
import com.zero.triptalk.reply.dto.response.ReplyResponse;
import com.zero.triptalk.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping("/detail/replies/{plannerDetailId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<ReplyGetResponse>> getRepliesByPlannerDetail(@PathVariable Long plannerDetailId) {

        return ResponseEntity.ok(replyService.getRepliesByPlannerDetailId(plannerDetailId));
    }

    @PostMapping("/detail/{plannerDetailId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ReplyResponse> ReplyOk(@PathVariable Long plannerDetailId,
                                                 @RequestBody ReplyRequest request, Principal principal) {

        return ResponseEntity.ok(replyService.replyOk(plannerDetailId,request, principal.getName()));
    }

    @PutMapping("/{replyId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ReplyResponse> ReplyUpdateOk(@PathVariable Long replyId,
                                                       @RequestBody ReplyRequest request, Principal principal) {

        return ResponseEntity.ok(replyService.replyUpdateOk(replyId,request, principal.getName()));
    }

    @DeleteMapping("/{replyId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ReplyResponse> ReplyDeleteOk(@PathVariable Long replyId, Principal principal) {

        return ResponseEntity.ok(replyService.replyDeleteOk(replyId, principal.getName()));
    }




}
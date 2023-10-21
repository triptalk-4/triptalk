package com.zero.triptalk.reply.dto.response;

import com.zero.triptalk.reply.entity.ReplyEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReplyGetResponse {
    private Long replyId;
    private String nickname;
    private String profile;
    private String reply;
    private LocalDateTime createDt;
    private String email;

    @Builder
    public ReplyGetResponse(Long replyId, String nickname, String profile, String reply, LocalDateTime createDt) {
        this.replyId = replyId;
        this.nickname = nickname;
        this.profile = profile;
        this.reply = reply;
        this.createDt = createDt;
    }

    public static ReplyGetResponse ofEntity(ReplyEntity reply) {
        return ReplyGetResponse.builder()
                .replyId(reply.getReplyId())
                .nickname(reply.getUser().getNickname())
                .profile(reply.getUser().getProfile())
                .reply(reply.getReply())
                .createDt(reply.getCreatedAt())
                .build();
    }
}

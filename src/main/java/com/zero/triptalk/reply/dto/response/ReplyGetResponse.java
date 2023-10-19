package com.zero.triptalk.reply.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyGetResponse {
    private Long replyId;
    private String name;
    private String profile;
    private String reply;
    private LocalDateTime createDt;

}

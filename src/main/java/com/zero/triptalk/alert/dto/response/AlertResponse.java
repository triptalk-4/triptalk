package com.zero.triptalk.alert.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertResponse {

    private long alertId;
    private Long plannerId;
    private String alertContent;
    private boolean userCheckYn;
    private String profile;
    private LocalDateTime alertDt;
}

package com.zero.triptalk.alert.dto.response;

import lombok.*;

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
}

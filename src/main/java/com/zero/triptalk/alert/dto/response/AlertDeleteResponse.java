package com.zero.triptalk.alert.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertDeleteResponse {

    private String deleteOneOk;
}

package com.zero.triptalk.like.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikenOnePlusMinusResponse {
    private String ok;
    private Long plannerCount;
    private Long detailPlannerCount;

}

package com.zero.triptalk.alert.controller;

import com.zero.triptalk.alert.dto.request.AlertUpdateResponse;
import com.zero.triptalk.alert.dto.response.AlertCntResponse;
import com.zero.triptalk.alert.dto.response.AlertResponse;
import com.zero.triptalk.alert.service.AlertService;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alert")
public class AlertController {

    private final AuthenticationService service;

    private final AlertService alertService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('USER')")
    public Page<AlertResponse> getAlert(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "6") int pageSize){
        UserEntity user = service.getUserByEmail();

        if (user != null) {
            Pageable pageable = PageRequest.of(page, pageSize);
            return alertService.getAlertAll(user, pageable);
        } else {
            // 유저가 존재하지 않을 경우 예외 처리
            return Page.empty();
        }
    }

    @GetMapping("/list/count")
    @PreAuthorize("hasAuthority('USER')")
    public AlertCntResponse getNewAlertCnt(){
        UserEntity user = service.getUserByEmail();

        return alertService.getAlertNewCnt(user);
    }

    @PutMapping("/update/{alertId}")
    @PreAuthorize("hasAuthority('USER')")
    public AlertUpdateResponse getNewAlertCnt(@PathVariable long alertId){

        return alertService.updateAlert(alertId);
    }

}

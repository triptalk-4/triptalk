package com.zero.triptalk.alert.service;

import com.zero.triptalk.alert.dto.request.AlertUpdateResponse;
import com.zero.triptalk.alert.dto.response.AlertCntResponse;
import com.zero.triptalk.alert.dto.response.AlertResponse;
import com.zero.triptalk.alert.entity.Alert;
import com.zero.triptalk.alert.repository.AlertRepository;
import com.zero.triptalk.exception.code.AlertErrorCode;
import com.zero.triptalk.exception.custom.AlertException;
import com.zero.triptalk.exception.custom.LikeException;
import com.zero.triptalk.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.zero.triptalk.exception.code.LikeErrorCode.No_User_Search;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;

    public Page<AlertResponse> getAlertAll(UserEntity user, Pageable pageable) {

        Page<Alert> alertPage = alertRepository.findByUser(user, pageable);

        Page<AlertResponse> alertResponses = alertPage.map(alert -> AlertResponse.builder()
                .alertId(alert.getAlertId())
                .plannerId(alert.getPlanner().getPlannerId())
                .alertContent(alert.getAlertContent())
                .userCheckYn(alert.isUserCheckYn())
                .build());

        return alertResponses;
    }

    public AlertCntResponse getAlertNewCnt(UserEntity user) {

        Long notSeeCount = alertRepository.countByUserAndUserCheckYnFalse(user);

        return AlertCntResponse.builder()
                .notSeeCount(notSeeCount)
                .build();

    }

    public AlertUpdateResponse updateAlert(Long alertId) {

        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new AlertException(AlertErrorCode.ALERT_NOT_FOUND));

        alert.setUserCheckYn(true);

        alertRepository.save(alert);

        return AlertUpdateResponse.builder()
                .updateOk("업데이트 완료")
                .build();



    }
}

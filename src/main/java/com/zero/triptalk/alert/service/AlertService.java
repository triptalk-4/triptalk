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

import java.util.Optional;

import static com.zero.triptalk.exception.code.LikeErrorCode.No_User_Search;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;

    public void markAlertAsChecked(long alertId) {
        Optional<Alert> alertOptional = alertRepository.findById(alertId);

        if (alertOptional.isPresent()) {
            Alert alert = alertOptional.get();
            alert.setUserCheckYn(true); // userCheckYn 값을 true로 설정

            alertRepository.save(alert); // 업데이트된 alert 엔티티를 저장
        }
    }

    public Page<AlertResponse> getAlertAll(UserEntity user, Pageable pageable) {

        Page<Alert> alertPage = alertRepository.findByUser(user, pageable);

        // 알림을 조회하면서 userCheckYn 값을 true로 변경
        alertPage.forEach(alert -> markAlertAsChecked(alert.getAlertId()));

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


}

package com.zero.triptalk.alert.service;

import com.zero.triptalk.alert.dto.request.AlertUpdateResponse;
import com.zero.triptalk.alert.dto.response.AlertCntResponse;
import com.zero.triptalk.alert.dto.response.AlertDeleteResponse;
import com.zero.triptalk.alert.dto.response.AlertResponse;
import com.zero.triptalk.alert.entity.Alert;
import com.zero.triptalk.alert.repository.AlertDeleteRepository;
import com.zero.triptalk.alert.repository.AlertRepository;
import com.zero.triptalk.exception.code.AlertErrorCode;
import com.zero.triptalk.exception.custom.AlertException;
import com.zero.triptalk.exception.custom.LikeException;
import com.zero.triptalk.exception.custom.UserException;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.zero.triptalk.exception.code.UserErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;

    private final AlertDeleteRepository alertDeleteRepository;

    private final UserRepository userRepository;

    public void markAlertAsChecked(long alertId) {
        Optional<Alert> alertOptional = alertRepository.findById(alertId);

        if (alertOptional.isPresent()) {
            Alert alert = alertOptional.get();
            alert.setUserCheckYn(true); // userCheckYn 값을 true로 설정

            alertRepository.save(alert); // 업데이트된 alert 엔티티를 저장
        }
    }

    public Page<AlertResponse> getAlertAll(UserEntity user, Pageable pageable) {

        Optional<UserEntity> alertOptional = userRepository.findByNickname(user.getNickname());

        Page<Alert> alertPage = alertRepository.findByNicknameOrderByAlertDtDesc(alertOptional.get().getNickname(), pageable);

        // 알림을 조회하면서 userCheckYn 값을 true로 변경
        alertPage.forEach(alert -> markAlertAsChecked(alert.getAlertId()));

        Page<AlertResponse> alertResponses = alertPage.map(alert -> AlertResponse.builder()
                .alertId(alert.getAlertId())
                .plannerId(alert.getPlanner().getPlannerId())
                .alertContent(alert.getAlertContent())
                .userCheckYn(alert.isUserCheckYn())
                .profile(alert.getUser().getProfile())
                .alertDt(alert.getAlertDt())
                .build());

        return alertResponses;
    }

    public AlertCntResponse getAlertNewCnt(UserEntity user) {

        Optional<UserEntity> alertOptional = userRepository.findByNickname(user.getNickname());

        Long notSeeCount = alertRepository.countByNicknameAndUserCheckYnFalse(alertOptional.get().getNickname());

        return AlertCntResponse.builder()
                .notSeeCount(notSeeCount)
                .build();

    }


    public AlertDeleteResponse deleteOneAlert(long alertId) {
        Optional<Alert> alertOptional = Optional.ofNullable(alertRepository.findById(alertId).orElseThrow(() ->
                new AlertException(AlertErrorCode.ALERT_NOT_FOUND)));

            Alert alert = alertOptional.get();

            alertRepository.delete(alert); // 업데이트된 alert 엔티티를 저장

            return AlertDeleteResponse.builder()
                    .deleteOneOk("삭제가 완료되었습니다.")
                    .build();

    }

    public AlertDeleteResponse deleteAllAlert(UserEntity user) {


        List<Alert> allAlerts = alertDeleteRepository.findByNickname(user.getNickname());

        // 알림을 삭제합니다.
        alertRepository.deleteAll(allAlerts);

        // 삭제된 알림 수나 다른 정보를 포함한 응답을 생성합니다.
        int deletedCount = allAlerts.size();

        return AlertDeleteResponse.builder()
                .deleteOneOk(deletedCount+"건의 알람이 삭제되었습니다.")
                .build();

    }
}

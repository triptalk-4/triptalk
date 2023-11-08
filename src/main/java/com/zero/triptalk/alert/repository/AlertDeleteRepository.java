package com.zero.triptalk.alert.repository;

import com.zero.triptalk.alert.entity.Alert;
import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertDeleteRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByUser(UserEntity user);

    List<Alert> findByNickname(String nickname);
}

package com.zero.triptalk.alert.repository;

import com.zero.triptalk.alert.entity.Alert;
import com.zero.triptalk.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    Page<Alert> findByUser(UserEntity user, Pageable pageable);

    Long countByUserAndUserCheckYnFalse(UserEntity user);
}

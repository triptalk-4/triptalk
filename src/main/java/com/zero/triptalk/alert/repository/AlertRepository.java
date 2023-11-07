package com.zero.triptalk.alert.repository;

import com.zero.triptalk.alert.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Long> {

}

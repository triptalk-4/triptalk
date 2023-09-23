package com.zero.triptalk.place.repository;

import com.zero.triptalk.place.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Images, Long> {
}

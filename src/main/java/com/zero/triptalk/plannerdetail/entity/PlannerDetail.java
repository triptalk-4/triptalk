package com.zero.triptalk.plannerdetail.entity;

import com.zero.triptalk.plannerdetail.dto.PlannerDetailRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@EntityListeners(value = {AuditingEntityListener.class})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlannerDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long plannerId;
    private Long userId;

    private LocalDate date;
    private LocalTime time;
    private String image;

    private String location;

    private String description;
    private Long views;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime modifiedAt;

    @Builder
    public PlannerDetail(Long plannerId, Long userId, LocalDate date, LocalTime time, String image, String location, String description) {
        this.plannerId = plannerId;
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.image = image;
        this.location = location;
        this.description = description;
    }

    public void updatePlannerDetail(PlannerDetailRequest request) {
        this.date = request.getDate();
        this.time = request.getTime();
        this.image = request.getImage();
        this.location = request.getLocation();
        this.description = request.getDescription();
    }
}

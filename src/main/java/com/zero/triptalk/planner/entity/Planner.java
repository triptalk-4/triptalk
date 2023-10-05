package com.zero.triptalk.planner.entity;

import com.zero.triptalk.planner.dto.PlannerStatus;
import com.zero.triptalk.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@EntityListeners(value = {AuditingEntityListener.class})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Planner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long plannerId;

    private String title;

    private String description;

    private String thumbnail;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private PlannerStatus plannerStatus;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;


}

package com.zero.triptalk.plannerdetail.entity;

import com.zero.triptalk.plannerdetail.dto.PlanStatus;
import com.zero.triptalk.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@EntityListeners(value = {AuditingEntityListener.class})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private PlanStatus planStatus;

    private LocalDateTime startDate;
    private LocalDateTime endDate;


}

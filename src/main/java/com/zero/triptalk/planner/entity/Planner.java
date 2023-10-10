package com.zero.triptalk.planner.entity;

import com.zero.triptalk.planner.type.PlannerStatus;
import com.zero.triptalk.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
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

    private Long views;

    //재훈님이 먼저 변경 후 나중에 Integer로 변경
    private Integer Likes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private PlannerStatus plannerStatus;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime startDate;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime endDate;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;

    public void increaseViews(){
        this.views++;
    }

}

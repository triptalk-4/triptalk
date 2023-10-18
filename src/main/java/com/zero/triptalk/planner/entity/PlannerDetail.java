package com.zero.triptalk.planner.entity;

import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.planner.dto.PlannerDetailRequest;
import com.zero.triptalk.planner.dto.UpdatePlannerDetailListRequest;
import com.zero.triptalk.user.entity.UserEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@EntityListeners(value = {AuditingEntityListener.class})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PlannerDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plannerDetailId;

    private Long userId;

    private String description;

    @ElementCollection
    private List<String> images;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "planner_id")
    private Planner planner;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime date;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime modifiedAt;

    @Builder
    public PlannerDetail(
            Long userId, String description, Place place,
            List<String> images, Planner planner, LocalDateTime date) {
        this.planner = planner;
        this.userId = userId;
        this.description = description;
        this.place = place;
        this.images = images;
        this.date = date;
    }

    public void updatePlannerDetail(UpdatePlannerDetailListRequest request,
                                    Planner planner, Place place, Long userId) {
        this.images = request.getImages();
        this.place = place;
        this.planner = planner;
        this.userId = userId;
        this.description = request.getDescription();
    }

    public static PlannerDetail buildPlannerDetail(
            Planner planner, PlannerDetailRequest request,
            UserEntity user, Place place, List<String> images) {
        return PlannerDetail.builder()
                .planner(planner)
                .userId(user.getUserId())
                .description(request.getDescription())
                .place(place)
                .images(images)
                .date(request.getDate())
                .build();
    }

}

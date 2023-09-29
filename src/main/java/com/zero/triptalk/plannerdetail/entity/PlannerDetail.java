package com.zero.triptalk.plannerdetail.entity;

import com.zero.triptalk.place.entity.Place;
import com.zero.triptalk.plannerdetail.dto.PlannerDetailRequest;
import com.zero.triptalk.user.entity.UserEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@EntityListeners(value = {AuditingEntityListener.class})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class PlannerDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long planId;
    private Long userId;

    private String description;

    private Long views;

//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "plannerDetail_id")
//    private List<Images> images;

    @ElementCollection
    private List<String> images;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;


    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime modifiedAt;

    @Builder
    public PlannerDetail(
            Long planId, Long userId,
            String description, Place place, List<String> images) {
        this.planId = planId;
        this.userId = userId;
        this.description = description;
        this.place = place;
        this.images = images;
    }

    public void updatePlannerDetail(PlannerDetailRequest request) {
        this.modifiedAt = request.getDate();
        this.description = request.getDescription();
    }

    public void updatePlannerDetailPlace(Place place) {
        this.place = place;
    }

    public static PlannerDetail buildPlannerDetail(
            Long planId, PlannerDetailRequest request,
            UserEntity user, Place place, List<String> images) {
        return PlannerDetail.builder()
                .planId(planId)
                .userId(user.getUserId())
                .description(request.getDescription())
                .place(place)
                .images(images)
                .build();
    }

}

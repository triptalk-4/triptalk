package com.zero.triptalk.reply.entity;

import com.zero.triptalk.base.BaseEntity;
import com.zero.triptalk.planner.entity.PlannerDetail;
import com.zero.triptalk.user.entity.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reply")
public class ReplyEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "planner_detail_Id")
    private PlannerDetail plannerDetail;

    private String reply;

    public void setReply(String reply) {
        this.reply = reply;
    }
}

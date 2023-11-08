package com.zero.triptalk.reply.service;

import com.zero.triptalk.alert.entity.Alert;
import com.zero.triptalk.alert.repository.AlertRepository;
import com.zero.triptalk.exception.custom.ReplyException;
import com.zero.triptalk.exception.custom.UserException;
import com.zero.triptalk.planner.entity.Planner;
import com.zero.triptalk.planner.entity.PlannerDetail;
import com.zero.triptalk.planner.repository.PlannerDetailRepository;
import com.zero.triptalk.planner.repository.PlannerRepository;
import com.zero.triptalk.reply.dto.request.ReplyRequest;
import com.zero.triptalk.reply.dto.response.ReplyResponse;
import com.zero.triptalk.reply.entity.ReplyEntity;
import com.zero.triptalk.reply.repository.ReplyRepository;
import com.zero.triptalk.user.entity.UserEntity;
import com.zero.triptalk.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReplyServiceTest {
    @InjectMocks private ReplyService replyService;
    @Mock private ReplyRepository replyRepository;
    @Mock private PlannerDetailRepository plannerDetailRepository;
    @Mock private UserRepository userRepository;

    @Mock private AlertRepository alertRepository;

    @Mock private PlannerRepository plannerRepository;

    @Test
    @DisplayName("댓글 수정 성공")
    void replyUpdateSuccess() {
        //given
        UserEntity user = UserEntity.builder()
                .email("test@email.com")
                .build();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        ReplyEntity replyEntity = ReplyEntity.builder()
                .reply("before")
                .user(user)
                .build();
        when(replyRepository.findById(any())).thenReturn(Optional.of(replyEntity));

        ReplyRequest request = new ReplyRequest("test");

        //when
        ReplyResponse replyResponse = replyService.replyUpdateOk(1L, request, "test@email.com");

        //then
        assertEquals("댓글 업데이트가 완료 되었습니다.", replyResponse.getPostOk());
        assertEquals("test", replyEntity.getReply());
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void replyDeleteSuccess() {
        //given
        UserEntity user = UserEntity.builder()
                .email("test@email.com")
                .build();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        ReplyEntity replyEntity = ReplyEntity.builder()
                .reply("before")
                .user(user)
                .build();
        when(replyRepository.findById(any())).thenReturn(Optional.of(replyEntity));

        //when
        ReplyResponse replyResponse = replyService.replyDeleteOk(1L, "test@email.com");

        //then
        assertEquals("댓글 삭제가 완료 되었습니다.", replyResponse.getPostOk());
    }

    @Test
    @DisplayName("댓글 등록 실패")
    void replyFail() {
        //given
        ReplyRequest request = new ReplyRequest("test");

        //when
        ReplyException exception = assertThrows(ReplyException.class, () ->
                    replyService.replyOk(1L, request, "test@email.com"));

        //then
        assertEquals("일치하는 게시글이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("댓글 수정 실패")
    void replyUpdateFail() {
        //given
        UserEntity user = UserEntity.builder()
                .email("test@email.com")
                .build();

        ReplyEntity replyEntity = ReplyEntity.builder()
                .reply("before")
                .user(user)
                .build();
        when(replyRepository.findById(any())).thenReturn(Optional.of(replyEntity));

        UserEntity anotherUser = UserEntity.builder()
                .email("anotherUser@email.com")
                .build();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(anotherUser));

        ReplyRequest request = new ReplyRequest("test");

        //when
        ReplyException exception = assertThrows(ReplyException.class, () ->
                replyService.replyUpdateOk(1L, request, "anotherUser@email.com"));

        //then
        assertEquals("댓글을 쓴 사람이 아닙니다.", exception.getMessage());
    }

    @Test
    @DisplayName("댓글 삭제 실패")
    void replyDeleteFail() {
        //given
        UserEntity user = UserEntity.builder()
                .email("test@email.com")
                .build();

        ReplyEntity replyEntity = ReplyEntity.builder()
                .reply("before")
                .user(user)
                .build();
        when(replyRepository.findById(any())).thenReturn(Optional.of(replyEntity));

        ReplyRequest request = new ReplyRequest("test");

        //when
        UserException exception = assertThrows(UserException.class, () ->
                replyService.replyUpdateOk(1L, request, "test@email.com"));

        //then
        assertEquals("이메일을 찾을 수 없습니다!", exception.getMessage());
    }
}
package com.zero.triptalk.user.repository;

import com.zero.triptalk.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String username);

    Optional<UserEntity> findByNickname(String nickname);

    @Query("select u.nickname from UserEntity u where u.nickname like %:keyword%")
    List<String> findByNicknameContains(@Param("keyword") String keyword);

}

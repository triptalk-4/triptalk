package com.zero.triptalk.user.repository;

import com.zero.triptalk.user.entity.UserDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UserSearchRepository extends ElasticsearchRepository<UserDocument, Long> {

    List<UserDocument> findByNicknameContains(String keyword, Pageable pageable);
}

package com.zero.triptalk.user.repository;

import com.zero.triptalk.user.entity.UserDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface UserSearchRepository extends ElasticsearchRepository<UserDocument, Long> {

    @Query("{\"match_phrase_prefix\": {\"nickname\": \"?0\"}}")
    List<UserDocument> findByNicknameContains(String keyword);
}

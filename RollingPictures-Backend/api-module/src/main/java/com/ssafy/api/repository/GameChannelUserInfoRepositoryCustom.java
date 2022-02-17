package com.ssafy.api.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameChannelUserInfoRepositoryCustom {
    Optional<Integer> findOrderNum(Long gameChannelId, Long userId);
}

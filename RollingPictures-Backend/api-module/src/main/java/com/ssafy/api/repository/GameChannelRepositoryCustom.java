package com.ssafy.api.repository;

import com.ssafy.api.domain.GameChannel;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameChannelRepositoryCustom {
    void deleteByChannelId(Long channelId);
}

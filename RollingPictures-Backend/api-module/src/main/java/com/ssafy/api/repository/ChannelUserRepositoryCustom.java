package com.ssafy.api.repository;

import com.ssafy.api.domain.ChannelUser;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelUserRepositoryCustom {
    ChannelUser findLeaderOfChannel(Long channelId);
}

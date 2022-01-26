package com.ssafy.api.repository;

import com.ssafy.api.domain.GameChannelUserOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface GameChannelUserOrderRepositoryCustom {
    GameChannelUserOrder findByUserid(Long userId);
}

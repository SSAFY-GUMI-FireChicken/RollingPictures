package com.ssafy.api.repository;

import com.ssafy.api.domain.GameChannelUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameChannelUserInfoRepository
        extends JpaRepository<GameChannelUserInfo, Long>, GameChannelUserInfoRepositoryCustom {
    GameChannelUserInfo findByUser_Id(Long userId);
}

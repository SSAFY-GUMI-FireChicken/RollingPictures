package com.ssafy.api.repository;

import com.ssafy.api.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Channel findTopByCode(String code);
}

package com.ssafy.api.repository;

import com.ssafy.api.domain.Channel;
import com.ssafy.api.domain.GameChannel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameChannelRepository extends JpaRepository<GameChannel, Long> {
    Optional<GameChannel> findByChannel(Channel channel);
}

package com.ssafy.api.repository;

import com.ssafy.api.domain.Channel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Channel findTopByCode(String code);

    @EntityGraph(attributePaths = {"channelUsers"})
    Optional<Channel> findChannelById(Long id);
}

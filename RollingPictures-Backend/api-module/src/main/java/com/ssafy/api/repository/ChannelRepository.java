package com.ssafy.api.repository;

import com.ssafy.api.domain.Channel;
import com.ssafy.core.code.YNCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Channel findTopByCode(String code);
    Channel findByGameChannel_Id(Long gameChannelId);

    long countByIsPublicAndIsPlaying(YNCode isPublic, YNCode isPlaying);

    List<Channel> findByIsPublicAndIsPlaying(YNCode isPublic, YNCode isPlaying, Pageable pageable);

    @EntityGraph(attributePaths = {"channelUsers"})
    Optional<Channel> findChannelById(Long id);
}

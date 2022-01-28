package com.ssafy.api.repository;

import com.ssafy.api.domain.GameChannel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameChannelRepository extends JpaRepository<GameChannel, Long> {
    @EntityGraph(attributePaths = {"gameChannelUserOrders", "sections"})
    GameChannel findGameChannelById(Long id);
}

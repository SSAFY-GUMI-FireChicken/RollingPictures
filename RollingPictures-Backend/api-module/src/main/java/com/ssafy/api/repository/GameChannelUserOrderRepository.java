package com.ssafy.api.repository;

import com.ssafy.api.domain.GameChannelUserOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameChannelUserOrderRepository
        extends JpaRepository<GameChannelUserOrder, Long>, GameChannelUserOrderRepositoryCustom {
}

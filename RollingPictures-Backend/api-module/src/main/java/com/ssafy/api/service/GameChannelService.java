package com.ssafy.api.service;

import com.ssafy.api.domain.GameChannel;
import com.ssafy.api.repository.GameChannelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GameChannelService {
    private final GameChannelRepository gameChannelRepository;

    public boolean isExistId(Long gameChannelId) {
        GameChannel findGameChannel = gameChannelRepository.findById(gameChannelId)
                .orElseGet(() -> GameChannel.builder().id(0L).code("").build());
        return findGameChannel.getId() != 0;
    }

    public void createGameChannel() {

    }
}

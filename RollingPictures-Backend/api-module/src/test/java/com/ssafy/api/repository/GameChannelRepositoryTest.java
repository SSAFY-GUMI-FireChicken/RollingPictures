package com.ssafy.api.repository;

import com.ssafy.api.domain.GameChannel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class GameChannelRepositoryTest {
    @Autowired
    GameChannelRepository gameChannelRepository;

    @Test
    void basicTest() {
//        GameChannel gameChannel = gameChannelRepository.save(GameChannel.builder()
//                .code("code")
//                .build());
//
//        GameChannel findGameChannel = gameChannelRepository.findGameChannelById(gameChannel.getId());
//        Assertions.assertThat(findGameChannel.getId()).isEqualTo(gameChannel.getId());
    }
}

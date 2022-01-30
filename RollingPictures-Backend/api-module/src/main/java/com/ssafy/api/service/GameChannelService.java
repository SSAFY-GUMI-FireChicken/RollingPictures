package com.ssafy.api.service;

import com.ssafy.api.domain.Channel;
import com.ssafy.api.domain.ChannelUser;
import com.ssafy.api.domain.GameChannel;
import com.ssafy.api.domain.GameChannelUserOrder;
import com.ssafy.api.dto.req.GameChannelCreateReqDTO;
import com.ssafy.api.dto.res.GameChannelCreateResDTO;
import com.ssafy.api.repository.ChannelRepository;
import com.ssafy.api.repository.GameChannelRepository;
import com.ssafy.core.exception.ApiMessageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GameChannelService {
    private final GameChannelRepository gameChannelRepository;
    private final ChannelRepository channelRepository;

    public boolean isExistId(Long gameChannelId) {
        GameChannel findGameChannel = gameChannelRepository.findById(gameChannelId)
                .orElseGet(() -> GameChannel.builder().id(0L).code("").build());
        return findGameChannel.getId() != 0;
    }

    @Transactional
    public GameChannelCreateResDTO createGameChannel(GameChannelCreateReqDTO dto) {
        Channel findChannel = channelRepository.findChannelById(dto.getChannelId())
                .orElseThrow(() -> new ApiMessageException("잘못된 채널입니다."));

        for (ChannelUser channelUser : findChannel.getChannelUsers()) {
            if ("Y".equals(channelUser.getIsLeader().getValue())) {
                long userId = channelUser.getUser().getId();
                if (userId != dto.getUserId()) {
                    throw new ApiMessageException("방장이 아닙니다.");
                }
            }
        }

        if (gameChannelRepository.findByChannel(findChannel).isPresent()) {
            throw new ApiMessageException("이미 게임방이 존재합니다.");
        }

        GameChannel gameChannel = GameChannel.builder()
                .channel(findChannel)
                .code(findChannel.getCode())
                .gameChannelUserOrders(new ArrayList<>())
                .build();

        int startOrder = 0;
        for (ChannelUser channelUser : findChannel.getChannelUsers()) {
            GameChannelUserOrder gameChannelUserOrder = GameChannelUserOrder.builder()
                    .user(channelUser.getUser())
                    .orderNum(startOrder++)
                    .build();

            gameChannel.addGameChannelUserOrder(gameChannelUserOrder);
        }
        GameChannel save = gameChannelRepository.save(gameChannel);
        return GameChannelCreateResDTO.builder()
                .id(save.getId())
                .build();
    }
}

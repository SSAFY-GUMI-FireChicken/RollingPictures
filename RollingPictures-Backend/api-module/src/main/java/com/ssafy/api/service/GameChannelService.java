package com.ssafy.api.service;

import com.ssafy.api.domain.*;
import com.ssafy.api.dto.req.GameChannelCreateReqDTO;
import com.ssafy.api.dto.res.GameChannelCreateResDTO;
import com.ssafy.api.repository.ChannelRepository;
import com.ssafy.api.repository.GameChannelRepository;
import com.ssafy.core.code.GamePlayState;
import com.ssafy.core.code.YNCode;
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

        if ("Y".equals(findChannel.getIsPlaying().getValue())) {
            throw new ApiMessageException("현재 게임 중인 방입니다.");
        } else {
            deleteGameChannel(dto.getChannelId());
        }

        for (ChannelUser channelUser : findChannel.getChannelUsers()) {
            if ("Y".equals(channelUser.getIsLeader().getValue())) {
                long userId = channelUser.getUser().getId();
                if (userId != dto.getUserId()) {
                    throw new ApiMessageException("방장이 아닙니다.");
                }
            }
        }

        GameChannel gameChannel = GameChannel.builder()
                .channel(findChannel)
                .code(findChannel.getCode())
                .conPeopleCnt(0)
                .curRoundNumber(1)
                .gameChannelUserInfos(new ArrayList<>())
                .build();

        int conPeopleNum = 0;
        int startOrder = 0;
        for (ChannelUser channelUser : findChannel.getChannelUsers()) {
            if (channelUser.getSessionId() != null && !channelUser.getSessionId().equals(null)) {
                conPeopleNum++;
            }

            channelUser.changeGamePlayState(GamePlayState.PLAYING);
            GameChannelUserInfo gameChannelUserOrder = GameChannelUserInfo.builder()
                    .user(channelUser.getUser())
                    .submitRoundNum(0)
                    .orderNum(startOrder++)
                    .build();

            gameChannel.addGameChannelUserInfo(gameChannelUserOrder);
        }

        gameChannel.changeConPeopleCnt(conPeopleNum);
        gameChannel.getChannel().changeIsPlaying(YNCode.Y);

        GameChannel save = gameChannelRepository.save(gameChannel);

        return GameChannelCreateResDTO.builder()
                .id(save.getId())
                .build();
    }

    public GameChannel findGameChannelByCode(String Code) throws Exception{
        GameChannel gameChannel = gameChannelRepository.findByCode(Code);
        return gameChannel;
    }

    @Transactional
    public void deleteGameChannel(Long channelId) {
        gameChannelRepository.deleteByChannel_Id(channelId);
    }
}

package com.ssafy.api.service;

import com.ssafy.api.domain.ChannelUser;
import com.ssafy.api.dto.res.UserInfoResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SocketService {

    @Autowired
    private SimpMessagingTemplate simpTemplate;

    public void sendInChannelUser(String channelCode, UserInfoResDTO userInfoResDTO) {
        simpTemplate.convertAndSend("/channel/in/" + channelCode, userInfoResDTO);
    }

    public void sendOutChannelUser(ChannelUser channelUser) {
        String channelCode = channelUser.getChannel().getCode();
        UserInfoResDTO userInfoResDTO = UserInfoResDTO.builder()
                .id(channelUser.getUser().getId())
                .nickname(channelUser.getUser().getNickname())
                .isLeader(channelUser.getIsLeader())
                .build();

        simpTemplate.convertAndSend("/channel/out/" + channelCode, userInfoResDTO);
    }

    public void sendChangingLeader(ChannelUser channelUser) {
        String channelCode = channelUser.getChannel().getCode();
        UserInfoResDTO userInfoResDTO = UserInfoResDTO.builder()
                .id(channelUser.getUser().getId())
                .nickname(channelUser.getUser().getNickname())
                .isLeader(channelUser.getIsLeader())
                .build();

        simpTemplate.convertAndSend("/channel/leader/" + channelCode, userInfoResDTO);
    }

    public void sendGameStart(String channelCode, Long gameChannelId) {
        simpTemplate.convertAndSend("/channel/start/" + channelCode, gameChannelId);
    }

    public void sendNextSignal(String channelCode, int roundNum) {
        simpTemplate.convertAndSend("/channel/game/next/" + channelCode, roundNum);
    }

    public void sendGameEnd(String channelCode, int roundNum) {
        simpTemplate.convertAndSend("/channel/end/" + channelCode, roundNum);
    }
}
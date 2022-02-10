package com.ssafy.api.service;

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

    public void sendOutChannelUser(String channelCode, UserInfoResDTO userInfoResDTO) {
        simpTemplate.convertAndSend("/channel/out/" + channelCode, userInfoResDTO);
    }

    public void sendChangingLeader(String channelCode, UserInfoResDTO userInfoResDTO) {
        simpTemplate.convertAndSend("/channel/leader/" + channelCode, userInfoResDTO);
    }

    public void sendStartGame(String channelCode, Long gameChannelId) {
        simpTemplate.convertAndSend("/channel/start/" + channelCode, gameChannelId);
    }

    public void sendNextSignal(String channelCode, int roundNum) {
        simpTemplate.convertAndSend("/channel/game/next/" + channelCode, roundNum);
    }
}
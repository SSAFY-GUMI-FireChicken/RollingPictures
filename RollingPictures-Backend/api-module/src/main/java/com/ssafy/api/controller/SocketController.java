package com.ssafy.api.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;


@RestController("/socket")
class SocketController {
    // 2022.01.31 임시 주석 처리
//    @MessageMapping("/enter/{channelCode}")
//    @SendTo("/enter/{channelCode}")
//    String enterChannel(String nickname, @DestinationVariable String channelCode) {
//        return nickname;
//    }
//
//    @MessageMapping("/exit/{channelCode}")
//    @SendTo("/exit/{channelCode}")
//    String exitChannel(String nickname, @DestinationVariable String channelCode) {
//        return nickname;
//    }
}

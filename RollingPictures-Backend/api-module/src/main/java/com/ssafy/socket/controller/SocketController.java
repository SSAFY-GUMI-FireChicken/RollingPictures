package com.ssafy.socket.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;


@RestController("/socket")
class SocketController {
    @MessageMapping("/enter/{channelCode}")
    @SendTo("/enter/{channelCode}")
    String enterChannel(String nickname, @DestinationVariable String channelCode) {
        return nickname;
    }

    @MessageMapping("/exit/{channelCode}")
    @SendTo("/exit/{channelCode}")
    String exitChannel(String nickname, @DestinationVariable String channelCode) {
        return nickname;
    }
}

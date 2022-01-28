package com.ssafy.api.controller;

import com.ssafy.api.dto.res.GameChannelResDTO;
import com.ssafy.api.service.GameChannelService;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"03. 게임방"})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gamechannel")
public class GameChannelController {
    private final ResponseService responseService;
    private final GameChannelService gameChannelService;

    @ApiOperation(value = "게임방 생성", notes = "게임방 생성")
    @PostMapping
    public SingleResult<GameChannelResDTO> createGameChannel(Long channelId) {


        return responseService.getSingleResult(GameChannelResDTO.builder().build());
    }
}

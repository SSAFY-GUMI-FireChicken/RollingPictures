package com.ssafy.api.controller;

import com.ssafy.api.dto.req.GameChannelCreateReqDTO;
import com.ssafy.api.dto.res.GameChannelCreateResDTO;
import com.ssafy.api.service.GameChannelService;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import com.ssafy.core.exception.ApiMessageException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public SingleResult<GameChannelCreateResDTO> createGameChannel(@RequestBody  @Valid GameChannelCreateReqDTO dto) {
        GameChannelCreateResDTO gameChannelCreateResDTO;
        try {
            gameChannelCreateResDTO = gameChannelService.createGameChannel(dto);
            return responseService.getSingleResult(gameChannelCreateResDTO);
        } catch (ApiMessageException e) {
            gameChannelCreateResDTO = GameChannelCreateResDTO.builder().id(-1L).build();
            return responseService.getSingleResult(gameChannelCreateResDTO);
        }
    }

    @ApiOperation(value = "게임방 삭제", notes = "게임방 삭제")
    @DeleteMapping("/{id}")
    public SingleResult<Boolean> deleteGameChannel(@PathVariable("id") Long gameChannleId) {
        return responseService.getSingleResult(true);
    }
}

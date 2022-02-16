package com.ssafy.api.controller;

import com.ssafy.api.domain.GameChannel;
import com.ssafy.api.domain.User;
import com.ssafy.api.dto.req.GameChannelCreateReqDTO;
import com.ssafy.api.dto.req.GameChannelGetReqDTO;
import com.ssafy.api.dto.res.GameChannelCreateResDTO;
import com.ssafy.api.dto.res.GameChannelGetResDTO;
import com.ssafy.api.dto.res.RoundResDTO;
import com.ssafy.api.service.GameChannelService;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import com.ssafy.core.exception.ApiMessageException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"04. 게임방"})
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

    @ApiOperation(value = "게임방 조회", notes = "게임방 조회")
    @GetMapping
    public SingleResult<GameChannelGetResDTO> getGameChannel(@RequestBody  @Valid GameChannelGetReqDTO req) throws Exception {

        GameChannel gameChannel = gameChannelService.findGameChannelByCode(req.getCode());
        return responseService.getSingleResult(GameChannelGetResDTO.builder()
                .id(gameChannel.getId())
                .curRoundNumber(gameChannel.getCurRoundNumber())
                .conPeopleCnt(gameChannel.getConPeopleCnt())
                .build());
    }

}

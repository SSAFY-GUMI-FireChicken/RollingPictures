package com.ssafy.api.controller;

import com.ssafy.api.dto.req.InOutChannelReqDTO;
import com.ssafy.api.service.ChannelUserService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.core.exception.ApiMessageException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Api(tags = {"03. 방유저"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/channeluser")
public class ChannelUserController {
    private final ChannelUserService channelUserService;
    private final ResponseService responseService;

    @Transactional
    @ApiOperation(value = "채널유저 상태 변경", notes = "게임 대기방으로 이동할 떄 채널 유저 상태 명시적으로 변경")
    @PutMapping
    public CommonResult changeChannelUserStatus(@RequestBody @Valid InOutChannelReqDTO req) throws ApiMessageException {
        try {
            channelUserService.changeChannelUserStatus(req);
        } catch (ApiMessageException apiMessageException) {
            throw apiMessageException;
        }

        return responseService.getSuccessResult();
    }
}

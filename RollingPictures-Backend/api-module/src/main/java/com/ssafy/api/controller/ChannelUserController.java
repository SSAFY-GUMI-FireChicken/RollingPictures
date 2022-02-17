package com.ssafy.api.controller;

import com.ssafy.api.dto.req.InOutChannelReqDTO;
import com.ssafy.api.service.ChannelUserService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.core.exception.ApiMessageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/channeluser")
public class ChannelUserController {
    private final ChannelUserService channelUserService;
    private final ResponseService responseService;

    @Transactional
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

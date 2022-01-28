package com.ssafy.api.controller;

import com.ssafy.api.domain.ChannelUser;
import com.ssafy.api.dto.req.InOutChannelReqDTO;
import com.ssafy.api.dto.req.MakeChannelReqDTO;
import com.ssafy.api.dto.res.ChannelResDTO;
import com.ssafy.api.dto.res.UserInfoResDTO;
import com.ssafy.api.service.ChannelService;
import com.ssafy.api.service.ChannelUserService;
import com.ssafy.api.service.SignService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import com.ssafy.core.code.YNCode;
import com.ssafy.api.domain.Channel;
import com.ssafy.api.domain.User;
import com.ssafy.core.exception.ApiMessageException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;

@Api(tags = {"02. 방"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/channel")
public class ChannelController {
    private final ChannelService channelService;
    private final ChannelUserService channelUserService;
    private final SignService signService;
    private final ResponseService responseService;

    private static final Logger logger = LoggerFactory.getLogger(ChannelController.class);

    @Transactional
    @ApiOperation(value = "방 생성", notes = "방 생성")
    @PostMapping
    public @ResponseBody SingleResult<ChannelResDTO> makeChannel(@Valid MakeChannelReqDTO req) throws Exception {
        Channel channelChk = null;

        User user = signService.findByUid(req.getUid(), YNCode.Y);
        if (user == null) {
            throw new ApiMessageException("회원 정보가 존재하지 않습니다.");
        }

        channelChk = channelService.createChannel();
        ArrayList<UserInfoResDTO> resUserList = channelUserService.createChannelUser(channelChk, user, YNCode.Y);

        return responseService.getSingleResult(ChannelResDTO
                .builder()
                .id(channelChk.getId())
                .code(channelChk.getCode())
                .users(resUserList)
                .build()
        );
    }

    @Transactional
    @ApiOperation(value = "방 입장", notes = "방 입장")
    @PostMapping(value = "/user")
    public SingleResult<ChannelResDTO> inChannel(@Valid InOutChannelReqDTO req) throws Exception {
        Channel channel = channelService.findByCode(req.getCode());
        if (channel == null) {
            throw new ApiMessageException("방 정보가 없습니다.");
        }

        User user = signService.findByUid(req.getUid(), YNCode.Y);
        if (user == null) {
            throw new ApiMessageException("회원 정보가 존재하지 않습니다.");
        }

        ChannelUser channelUser = channelUserService.findByUser(user);
        if (channelUser != null) {
            throw new ApiMessageException("이미 방에 입장한 상태입니다.");
        }

        ArrayList<UserInfoResDTO> resUserList = channelUserService.createChannelUser(channel, user, YNCode.N);

        return responseService.getSingleResult(ChannelResDTO
                .builder()
                .id(channel.getId())
                .code(channel.getCode())
                .users(resUserList)
                .build()
        );
    }

    @Transactional
    @ApiOperation(value = "방 퇴장", notes = "방 퇴장")
    @DeleteMapping(value = "/user")
    public CommonResult outChannel(@Valid InOutChannelReqDTO req) throws Exception {
        User user = signService.findByUid(req.getUid(), YNCode.Y);
        if (user == null) {
            throw new ApiMessageException("회원 정보가 존재하지 않습니다.");
        }

        ChannelUser channelUser = channelUserService.findByUser(user);
        if (channelUser == null) {
            throw new ApiMessageException("방에 입장한 상태가 아닙니다.");
        }

        channelUserService.deleteChannelUser(channelUser);

        return responseService.getSuccessResult();
    }
}
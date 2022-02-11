package com.ssafy.api.controller;

import com.ssafy.api.domain.ChannelUser;
import com.ssafy.api.dto.req.InOutChannelReqDTO;
import com.ssafy.api.dto.req.MakeChannelReqDTO;
import com.ssafy.api.dto.res.ChannelListResDTO;
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
    public @ResponseBody SingleResult<ChannelResDTO> makeChannel(@RequestBody @Valid MakeChannelReqDTO req) throws Exception {
        Channel channelChk = null;

        User user = signService.findUserById(req.getId());
        if (user == null) {
            throw new ApiMessageException("회원 정보가 존재하지 않습니다.");
        }

        ChannelUser channelUser = channelUserService.findByUser(user);
        if (channelUser != null) {
            throw new ApiMessageException("이미 방에 입장한 상태입니다.");
        }

        if (req.getMaxPeopleCnt() > 6) {
            throw new ApiMessageException("최대 인원 수를 6 이상으로 설정할 수 없습니다.");
        }

        channelChk = channelService.createChannel(req);
        ArrayList<UserInfoResDTO> resUserList = channelUserService.createChannelUser(channelChk, user, YNCode.Y);

        return responseService.getSingleResult(ChannelResDTO
                .builder()
                .id(channelChk.getId())
                .code(channelChk.getCode())
                .isPublic(channelChk.getIsPublic())
                .curPeopleCnt(channelChk.getCurPeopleCnt())
                .maxPeopleCnt(channelChk.getMaxPeopleCnt())
                .title(channelChk.getTitle())
                .users(resUserList)
                .build()
        );
    }

    @Transactional
    @ApiOperation(value = "방 입장", notes = "방 입장")
    @PostMapping(value = "/user")
    public SingleResult<ChannelResDTO> inChannel(@RequestBody @Valid InOutChannelReqDTO req) throws Exception {
        Channel channel = channelService.findByCode(req.getCode());
        if (channel == null) {
            throw new ApiMessageException("방 정보가 없습니다.");
        } else if (channel.getIsPlaying() == YNCode.Y) {
            throw new ApiMessageException("게임 중인 방은 입장할 수 없습니다.");
        } else if (channel.getCurPeopleCnt() == channel.getMaxPeopleCnt()) {
            throw new ApiMessageException("정원 초과로 입장할 수 없습니다.");
        }

        User user = signService.findUserById(req.getId());
        if (user == null) {
            throw new ApiMessageException("회원 정보가 존재하지 않습니다.");
        }

        ChannelUser channelUser = channelUserService.findByUser(user);
        if (channelUser != null) {
            throw new ApiMessageException("이미 방에 입장한 상태입니다.");
        }

        ArrayList<UserInfoResDTO> resUserList = channelUserService.createChannelUser(channel, user, YNCode.N);

        return responseService.getSingleResult(ChannelResDTO.builder()
                .id(channel.getId())
                .code(channel.getCode())
                .isPublic(channel.getIsPublic())
                .curPeopleCnt(channel.getCurPeopleCnt())
                .maxPeopleCnt(channel.getMaxPeopleCnt())
                .title(channel.getTitle())
                .users(resUserList)
                .build()
        );
    }

    @Transactional
    @ApiOperation(value = "방 퇴장", notes = "방 퇴장")
    @DeleteMapping(value = "/user")
    public CommonResult outChannel(@RequestBody @Valid InOutChannelReqDTO req) throws Exception {
        User user = signService.findUserById(req.getId());
        if (user == null) {
            throw new ApiMessageException("회원 정보가 존재하지 않습니다.");
        }

        ChannelUser channelUser = channelUserService.findByUser(user);
        if (channelUser == null) {
            throw new ApiMessageException("방에 입장한 상태가 아닙니다.");
        } else if (channelUser.getChannel().getIsPlaying() == YNCode.Y) {
            throw new ApiMessageException("게임 중인 방은 퇴장할 수 없습니다.");
        }

        channelUserService.deleteChannelUser(channelUser);

        return responseService.getSuccessResult();
    }

    @Transactional
    @ApiOperation(value = "공개방 목록 조회", notes = "공개방 목록 조회")
    @GetMapping
    public SingleResult<ChannelListResDTO> getChannelList(@RequestParam(value="page", defaultValue = "0") int page,
                                                          @RequestParam(value="batch" , defaultValue = "10") int batch) {
        return responseService.getSingleResult(channelService.getChannelList(page, batch));
    }

    @Transactional
    @ApiOperation(value = "방 설정 수정", notes = "방 설정 수정")
    @PutMapping
    public @ResponseBody SingleResult<ChannelResDTO> changeChannelOption(@RequestBody @Valid MakeChannelReqDTO req) {
        try {
            return responseService.getSingleResult(channelService.changeChannelOption(req));
        } catch (ApiMessageException apiMessageException) {
            throw apiMessageException;
        }
    }
}

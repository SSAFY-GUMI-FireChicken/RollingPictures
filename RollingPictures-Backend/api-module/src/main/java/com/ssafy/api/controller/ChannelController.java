package com.ssafy.api.controller;

import com.ssafy.api.dto.req.InOutChannelReqDTO;
import com.ssafy.api.dto.req.MakeChannelReqDTO;
import com.ssafy.api.dto.res.ChannelResDTO;
import com.ssafy.api.dto.res.Result;
import com.ssafy.api.dto.res.SectionResDTO;
import com.ssafy.api.dto.res.UserInfoResDTO;
import com.ssafy.api.service.ChannelService;
import com.ssafy.api.service.SignService;
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
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Api(tags = {"03. 방"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/channel")
public class ChannelController {
    private final ChannelService channelService;
    private final SignService signService;
    private final ResponseService responseService;

    @Transactional
    @ApiOperation(value = "방 생성", notes = "방 생성")
    @PostMapping
    public @ResponseBody SingleResult<ChannelResDTO> makeChannel(@Valid MakeChannelReqDTO req) throws Exception {
        String code = "";
        Channel channelChk = null;

        User user = signService.findByUid(req.getUid(), YNCode.Y);
        if (user == null) {
            throw new ApiMessageException("회원 정보가 존재하지 않습니다.");
        }

        do {
            code = RandomStringUtils.randomAlphanumeric(6);
            channelChk = channelService.findByCode(code);
        } while (channelChk != null);

        Channel channel = Channel.builder()
                .maxPeopleCnt(6)
                .curPeopleCnt(1)
                .roundCount(60)
                .isPlaying(false)
                .leaderId(user.getId())
                .code(code)
                .roundStartTime(LocalDateTime.now())
                .build();

        user.setChannel(channel);
        signService.saveUser(user);
        Long chkId = channelService.saveChannel(channel);

        return responseService.getSingleResult(ChannelResDTO
                .builder()
                .id(chkId)
                .code(code)
                .build()
        );
    }

    @Transactional
    @ApiOperation(value = "방 입/퇴장", notes = "방 입/퇴장")
    @PutMapping
    public SingleResult<ChannelResDTO> inOutChannel(@Valid InOutChannelReqDTO req) throws Exception {
        Channel channel = channelService.findByCode(req.getCode());
        if (channel == null) {
            throw new ApiMessageException("방 정보가 없습니다.");
        }

        User user = signService.findByUid(req.getUid(), YNCode.Y);
        if (user == null) {
            throw new ApiMessageException("회원 정보가 존재하지 않습니다.");
        }

        if (req.isEntrance()) {
            ArrayList<UserInfoResDTO> resUserList = new ArrayList<UserInfoResDTO>();

            channel.setCurPeopleCnt(channel.getCurPeopleCnt() + 1);
            channel.addUser(user);
            user.setChannel(channel);

            for (User curUser : channel.getUsers()) {
                resUserList.add(UserInfoResDTO
                        .builder()
                        .uid(curUser.getUid())
                        .nickname(curUser.getNickname())
                        .build()
                );
            }

            return responseService.getSingleResult(ChannelResDTO.builder()
                    .id(channel.getId())
                    .code(channel.getCode())
                    .users(resUserList)
                    .leaderId(channel.getLeaderId())
                    .build());
        } else {
            user.setChannel(null);

            if (channel.getCurPeopleCnt() == 1) {
                channelService.deleteChannel(channel);
            } else {
                if (channel.getLeaderId() == user.getId()) {
                    channel.getUsers().remove(user);
                    User nextLeader = channel.getUsers().get(0);
                    channel.setLeaderId(nextLeader.getId());
                }

                channel.setCurPeopleCnt(channel.getCurPeopleCnt() - 1);
            }

            return responseService.getSingleResult(ChannelResDTO
                    .builder()
                    .id(channel.getId())
                    .build()
            );
        }
    }

    @PostMapping("/api/section")
    public Result<List<SectionResDTO>> section() throws Exception {

        return new Result<>();
    }
}

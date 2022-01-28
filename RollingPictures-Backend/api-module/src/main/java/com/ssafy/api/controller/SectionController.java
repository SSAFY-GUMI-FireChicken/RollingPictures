package com.ssafy.api.controller;

import com.ssafy.api.dto.res.SectionResDTO;
import com.ssafy.api.service.GameChannelService;
import com.ssafy.api.service.SectionService;
import com.ssafy.api.service.common.ListResult;
import com.ssafy.api.service.common.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(tags = {"05. 섹션"})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/section")
public class SectionController {
    private final ResponseService responseService;
    private final SectionService sectionService;
    private final GameChannelService gameChannelService;

    @ApiOperation(value = "섹션 조회", notes = "특정 시작 유저에 대한 섹션 조회")
    @PostMapping
    public ListResult<SectionResDTO> section(Long gameChannelId, Long userId) throws Exception {
        if (!gameChannelService.isExistId(gameChannelId)) {
            log.info("해당 게임방이 존재하지 않음");
            return responseService.getListResult(new ArrayList<>(), "요청하신 채널 또는 아이디가 잘못되었습니다.");
        }
        List<SectionResDTO> section = sectionService.getSection(gameChannelId, userId);
        log.info("aaa");
        return responseService.getListResult(section);
    }
}

package com.ssafy.api.controller;

import com.ssafy.api.dto.req.SectionCreateReqDTO;
import com.ssafy.api.dto.res.SectionCreateResDTO;
import com.ssafy.api.dto.res.SectionResDTO;
import com.ssafy.api.service.GameChannelService;
import com.ssafy.api.service.SectionService;
import com.ssafy.api.service.common.ListResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.core.exception.ApiMessageException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Api(tags = {"04. 섹션"})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/section")
public class SectionController {
    private final ResponseService responseService;
    private final SectionService sectionService;
    private final GameChannelService gameChannelService;

    @ApiOperation(value = "섹션 생성", notes = "해당 게임방의 섹션을 생성합니다.")
    @PostMapping
    public ListResult<SectionCreateResDTO> create(@Valid SectionCreateReqDTO dto) {
        List<SectionCreateResDTO> result;
        try {
            result = sectionService.createSection(dto);
            return responseService.getListResult(result);
        } catch (ApiMessageException e) {
            return responseService.getListResult(new ArrayList<>());
        } catch (Exception e) {
            return responseService.getListResult(new ArrayList<>());
        }
    }

    @ApiOperation(value = "섹션 조회", notes = "특정 시작 유저에 대한 섹션 조회")
    @GetMapping
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

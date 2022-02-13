package com.ssafy.api.controller;

import com.ssafy.api.domain.Round;
import com.ssafy.api.domain.User;
import com.ssafy.api.dto.req.RoundReqDTO;
import com.ssafy.api.dto.req.SectionCreateReqDTO;
import com.ssafy.api.dto.req.SectionDeleteReqDTO;
import com.ssafy.api.dto.res.*;
import com.ssafy.api.service.GameChannelService;
import com.ssafy.api.service.RoundService;
import com.ssafy.api.service.SectionService;
import com.ssafy.api.service.SignService;
import com.ssafy.api.service.common.ListResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.S3Uploader;
import com.ssafy.api.service.common.SingleResult;
import com.ssafy.core.code.YNCode;
import com.ssafy.core.exception.ApiMessageException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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
    private final S3Uploader s3Uploader;

    @ApiOperation(value = "섹션 생성", notes = "해당 게임방의 섹션을 생성합니다.")
    @PostMapping
    public ListResult<SectionCreateResDTO> create(@RequestBody @Valid SectionCreateReqDTO dto) {
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
    public ListResult<SectionRetrieveResDTO> section(Long gameChannelId, Long userId) {
        try {
            List<SectionRetrieveResDTO> section = sectionService.getSection(gameChannelId, userId);
            return responseService.getListResult(section);
        } catch (ApiMessageException e) {
            return responseService.getListResult(new ArrayList<>());
        } catch (Exception e) {
            return responseService.getListResult(new ArrayList<>());
        }
    }

    @ApiOperation(value = "섹션 조회", notes = "게임방의 모든 섹션을 조회합니다.")
    @GetMapping("/all")
    public ListResult<SectionAllRetrieveResDTO> section(Long gameChannelId) {
        try {
            List<SectionAllRetrieveResDTO> section = sectionService.getSection(gameChannelId);
            return responseService.getListResult(section);
        } catch (ApiMessageException e) {
            return responseService.getListResult(new ArrayList<>());
        } catch (Exception e) {
            return responseService.getListResult(new ArrayList<>());
        }
    }

    @ApiOperation(value = "섹션 삭제", notes = "섹션 삭제")
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String Test(
            @RequestBody @Valid SectionDeleteReqDTO req) throws Exception {

        String result = s3Uploader.delete(req.getCode());
        return result;
    }
}
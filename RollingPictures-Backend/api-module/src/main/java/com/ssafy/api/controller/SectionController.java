package com.ssafy.api.controller;

import com.ssafy.api.dto.req.SectionCreateReqDTO;
import com.ssafy.api.dto.req.SectionDeleteReqDTO;
import com.ssafy.api.dto.res.*;
import com.ssafy.api.service.SectionService;
import com.ssafy.api.service.common.ListResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.S3Uploader;
import com.ssafy.core.exception.ApiMessageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/section")
public class SectionController {
    private final ResponseService responseService;
    private final SectionService sectionService;
    private final S3Uploader s3Uploader;

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

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String Test(
            @RequestBody @Valid SectionDeleteReqDTO req) throws Exception {

        String result = s3Uploader.delete(req.getCode());
        return result;
    }
}
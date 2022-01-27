//package com.ssafy.api.controller;
//
//import com.ssafy.api.dto.res.SectionResDTO;
//import com.ssafy.api.service.SectionService;
//import com.ssafy.api.service.common.ListResult;
//import com.ssafy.api.service.common.ResponseService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Api(tags = {"04. 섹션"})
//@Slf4j
//@RequiredArgsConstructor
//@RestController
//public class SectionController {
//    private final ResponseService responseService;
//    private final SectionService sectionService;
//
//    @ApiOperation(value = "섹션 조회", notes = "특정 시작 유저에 대한 섹션 조회")
//    @PostMapping("/section")
//    public ListResult<SectionResDTO> section(Long gameChannelId, Long userId) throws Exception {
//        List<SectionResDTO> section = sectionService.getSection(gameChannelId, userId);
//        return responseService.getListResult(section);
//    }
//}

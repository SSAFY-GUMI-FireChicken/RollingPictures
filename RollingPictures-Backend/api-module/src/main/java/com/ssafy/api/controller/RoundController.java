package com.ssafy.api.controller;

import com.ssafy.api.domain.Round;
import com.ssafy.api.domain.Section;
import com.ssafy.api.domain.User;
import com.ssafy.api.dto.req.RoundReqDTO;
import com.ssafy.api.dto.res.RoundResDTO;
import com.ssafy.api.repository.SectionRepository;
import com.ssafy.api.service.RoundService;
import com.ssafy.api.service.SectionService;
import com.ssafy.api.service.SignService;
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
import javax.validation.constraints.Null;
import java.io.IOException;

@Api(tags = {"05. 라운드"})
@Slf4j
@RequiredArgsConstructor // Lombok 라이브러리, final 필드에 대한 생성자를 생성함.
@RestController
@RequestMapping(value = "/api/round")
public class RoundController {

    private final S3Uploader s3Uploader;
    private final RoundService roundService;
    private final ResponseService responseService;
    private final SignService signService;
    private final SectionRepository sectionRepository;

//    @PostMapping("/images")
//    public String upload(@RequestParam("images") MultipartFile multipartFile) throws IOException {
//        System.out.println(multipartFile);
//        return s3Uploader.upload(multipartFile, "static");
//    }

    @ApiOperation(value = "라운드 등록", notes = "라운드 등록")
    @PostMapping(value="/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<RoundResDTO> Test(
            @Valid RoundReqDTO req,
            @RequestParam(value="이미지", required = false) MultipartFile multipartFile) throws Exception {

        User currentUser = signService.findByUid(req.getUid(), YNCode.Y);
        User hostUser = signService.findUserById(req.getHostId());
        String img = req.getKeyword();
        Section section = sectionRepository.findSection(req.getGameChannelId(),req.getHostId())
                .orElseThrow(() -> new ApiMessageException("찾을 수 없는 섹션입니다."));
        if ( multipartFile != null ) {
            img = s3Uploader.upload(multipartFile, section.getCode()+"/"+hostUser.getId()+"/"+req.getRoundNumber()+"ROUND-"+currentUser.getId()+".JPG");
        }


        Round round = Round.builder()
                .roundNumber(req.getRoundNumber())
                .imgSrc(img)
                .section(section)
                .user(currentUser)
                .build();
        long roundId = roundService.post(round);
        return responseService.getSingleResult(RoundResDTO.builder().id(roundId).build());
    }


}

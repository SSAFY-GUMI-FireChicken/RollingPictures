package com.ssafy.api.controller;

import com.ssafy.api.domain.Round;
import com.ssafy.api.dto.req.RoundReqDTO;
import com.ssafy.api.dto.res.RoundResDTO;
import com.ssafy.api.service.RoundService;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.S3Uploader;
import com.ssafy.api.service.common.SingleResult;
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

    @PostMapping("/images")
    public String upload(@RequestParam("images") MultipartFile multipartFile) throws IOException {
        System.out.println(multipartFile);
        return s3Uploader.upload(multipartFile, "static");
    }

    @ApiOperation(value = "라운드 등록", notes = "라운드 등록")
    @PostMapping(value="/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<RoundResDTO> Test(
            @RequestBody @Valid RoundReqDTO req,
            @RequestPart(value="이미지", required = false) MultipartFile multipartFile) throws IOException {
        String img = req.getKeyword();
        if (multipartFile != null) {
            img = s3Uploader.upload(multipartFile, "static");
        }

        Round round = Round.builder()
                .roundNumber(req.getRoundNumber())
                .imgSrc(img)
                .isKeyword(req.getIsKeyword())
                .build();
        long roundId = roundService.post(round);
        return responseService.getSingleResult(RoundResDTO.builder().id(roundId).build());
    }


}

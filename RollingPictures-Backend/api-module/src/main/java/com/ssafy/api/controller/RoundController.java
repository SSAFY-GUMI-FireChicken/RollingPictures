package com.ssafy.api.controller;

import com.ssafy.api.domain.Round;
import com.ssafy.api.domain.Section;
import com.ssafy.api.domain.User;
import com.ssafy.api.dto.req.RoundReqDTO;
import com.ssafy.api.dto.res.RoundResDTO;
import com.ssafy.api.repository.RoundRepository;
import com.ssafy.api.repository.SectionRepository;
import com.ssafy.api.service.RoundService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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


    @ApiOperation(value = "라운드 등록", notes = "라운드 등록")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<RoundResDTO> roundRegister(
            @Valid RoundReqDTO req,
            @RequestParam(value="이미지", required = false) MultipartFile multipartFile) throws Exception {

        User currentUser = signService.findByUid(req.getUid(), YNCode.Y);
        List<User> userOrders = sectionRepository.findOrder(req.getGameChannelId());
        int currentIndex = 0;
        for (int i = 0; i < userOrders.size(); i++) {
            if (Objects.equals(currentUser.getUid(), userOrders.get(i).getUid())) {
                currentIndex = i;
            }
        }
        int hostIndex = (currentIndex+req.getRoundNumber()-1)%userOrders.size();

        Section section = sectionRepository.findSection(req.getGameChannelId(), userOrders.get(hostIndex).getId())
                .orElseThrow(() -> new ApiMessageException("찾을 수 없는 섹션입니다."));
        String img = req.getKeyword();
        if (multipartFile != null) {
            img = s3Uploader.upload(multipartFile, section.getCode() + "/" + userOrders.get(hostIndex).getId() + "/" + req.getRoundNumber() + "ROUND-" + currentUser.getId() + ".JPG");
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

    @ApiOperation(value = "라운드 조회", notes = "라운드 조회")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<RoundResDTO> roundView(@Valid RoundReqDTO req) throws Exception {

        User currentUser = signService.findByUid(req.getUid(), YNCode.Y);
        List<User> userOrders = sectionRepository.findOrder(req.getGameChannelId());
        int currentIndex = 0;
        for (int i = 0; i < userOrders.size(); i++) {
            if (Objects.equals(currentUser.getUid(), userOrders.get(i).getUid())) {
                currentIndex = i;
            }
        }
        int hostIndex = (currentIndex+req.getRoundNumber()-1)%userOrders.size();

        Section section = sectionRepository.findSection(req.getGameChannelId(), userOrders.get(hostIndex).getId())
                .orElseThrow(() -> new ApiMessageException("찾을 수 없는 섹션입니다."));

        List<Round> sectionRounds = sectionRepository.findRounds(section.getId());
        int count = -1;
        if (req.getRoundNumber()%2==1){
            for (int i=1;i<sectionRounds.size();i+=2) {
                if (sectionRounds.get(i).getImgSrc() != null) {
                    count = i;
                }
            }
        } else if (req.getRoundNumber() % 2 == 0){
            for (int i=0;i<sectionRounds.size();i+=2){
                if(sectionRounds.get(i).getImgSrc() != null){
                    count = i;
                }
            }
        }
        System.out.println(section.getId());
        String imgSrc = "";
        if (count != -1){
            imgSrc = sectionRounds.get(count).getImgSrc();
        }
        return responseService.getSingleResult(RoundResDTO.builder().imgSrc(imgSrc).build());
    }

}

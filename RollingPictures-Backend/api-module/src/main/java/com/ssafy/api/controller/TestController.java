package com.ssafy.api.controller;

import com.ssafy.api.dto.req.TestReqDTO;
import com.ssafy.api.dto.res.TestResDTO;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import com.ssafy.api.service.TestService;
import com.ssafy.core.entity.Test;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Api(tags = {"01. 테스트"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/test")
public class TestController {
    private final TestService testService;
    private final ResponseService responseService;

    @ApiOperation(value = "메시지", notes = "메시지")
    @GetMapping(value="/messages",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody // public @ResponseBody String testByResponseBody()와 같이 리턴 타입 좌측에 지정 가능
    public String testByResponseBody()  throws Exception {
        String message = "안녕하세요. 잠시 후에 화면에서 만나요!";
        return message;
    }

    @ApiOperation(value = "메시지모두", notes = "메시지모두")
    @GetMapping(value="/notice",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Test> findAll(@RequestParam(required = false, name = "q") String query,
                              @PageableDefault Pageable pageable) {
        return testService.findAll(query, pageable);
    }

    @ApiOperation(value = "메시지 작성", notes = "메시지 작성")
    @PostMapping(value="/notice",produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody SingleResult<TestResDTO> Test (@Valid TestReqDTO req) {
        Test test = Test.builder()
                .title(req.getTitle())
                .content((req.getContent()))
                .build();

        long testId = testService.post(test);
        return responseService.getSingleResult(TestResDTO.builder().id(testId).build());

    }

    @ApiOperation(value = "메시지하나", notes = "메시지하나")
    @GetMapping(value="/notice/{testSeq}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Test> findById(@PathVariable Long testSeq) {

        return testService.findById(testSeq);
    }

    @ApiOperation(value = "메시지 삭제", notes = "메시지 삭제")
    @DeleteMapping(value="/notice/{testSeq}",produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteOne(@PathVariable Long testSeq) {
        testService.delete(testSeq);
    }

}

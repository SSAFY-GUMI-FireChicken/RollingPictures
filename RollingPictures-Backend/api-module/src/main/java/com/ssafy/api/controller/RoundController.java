package com.ssafy.api.controller;

import com.ssafy.api.service.common.S3Uploader;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(tags = {"03. 라운드"})
@Slf4j
@RequiredArgsConstructor // Lombok 라이브러리, final 필드에 대한 생성자를 생성함.
@RestController
@RequestMapping(value = "/api/round")
public class RoundController {

    private final S3Uploader s3Uploader;

    @PostMapping("/images")
    public String upload(@RequestParam("images") MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile, "static");
    }

}

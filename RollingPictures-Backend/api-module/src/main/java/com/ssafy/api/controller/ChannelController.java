package com.ssafy.api.controller;

import com.ssafy.api.dto.res.Result;
import com.ssafy.api.dto.res.SectionResDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChannelController {



    @PostMapping("/api/section")
    public Result<List<SectionResDTO>> section() throws Exception {

        return new Result<>();
    }
}

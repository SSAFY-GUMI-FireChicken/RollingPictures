package com.ssafy.api.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestReqDTO {

    @ApiModelProperty(value = "타이틀", required = true, example = "아잉")
    private String title;

    @ApiModelProperty(value = "내용", required = true, example = "케케")
    private String content;

}

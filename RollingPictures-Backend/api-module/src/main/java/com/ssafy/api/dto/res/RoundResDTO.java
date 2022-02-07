package com.ssafy.api.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoundResDTO {
    @ApiModelProperty(value = "테스트 아이디", required = true, example = "1")
    private long id;

    @ApiModelProperty(value = "키워드", required = true, example = "1")
    private String imgSrc;



}
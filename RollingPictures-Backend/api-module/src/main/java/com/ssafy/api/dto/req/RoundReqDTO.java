package com.ssafy.api.dto.req;

import com.ssafy.core.code.YNCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoundReqDTO {

    @ApiModelProperty(value = "현재 라운드", required = true, example = "2")
    private Integer roundNumber;

    @ApiModelProperty(value = "키워드", required = false, example = "하얗고 순백한 석규")
    private String keyword;

    @ApiModelProperty(value = "키워드여부", required = true, example = "Y")
    private YNCode isKeyword;

}

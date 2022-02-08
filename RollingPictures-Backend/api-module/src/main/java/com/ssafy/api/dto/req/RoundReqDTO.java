package com.ssafy.api.dto.req;

import com.ssafy.core.code.YNCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;


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

    @ApiModelProperty(value = "기기 유저 ID", required = true, example = "1")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "게임방 ID", required = true, example = "0")
    private Long gameChannelId;

}

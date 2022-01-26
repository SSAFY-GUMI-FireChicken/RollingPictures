package com.ssafy.api.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InOutChannelReqDTO {
    @NotNull
    @ApiModelProperty(value = "유저 uid(SSAID)", required = true, example = "")
    private String uid;

    @NotNull
    @ApiModelProperty(value = "채널 코드", required = false, example = "X9vqV0")
    private String code;
}

package com.ssafy.api.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MakeChannelReqDTO {
    @NotNull
    @ApiModelProperty(value = "유저 uid(SSAID)", required = true, example = "")
    private String uid;
}

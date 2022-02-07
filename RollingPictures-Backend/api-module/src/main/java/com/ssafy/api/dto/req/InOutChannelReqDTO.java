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
    @ApiModelProperty(value = "유저 id", required = true, example = "1")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "채널 코드", required = false, example = "X9vqV0")
    private String code;
}

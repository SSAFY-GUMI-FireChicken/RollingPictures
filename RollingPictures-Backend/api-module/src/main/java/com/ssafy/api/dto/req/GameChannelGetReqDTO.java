package com.ssafy.api.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameChannelGetReqDTO {
    @ApiModelProperty(value = "방 코드", required = true, example = "1")
    private String code;
}

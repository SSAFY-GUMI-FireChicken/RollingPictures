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
    @ApiModelProperty(value = "유저 id", required = true, example = "1")
    private Long id;
}

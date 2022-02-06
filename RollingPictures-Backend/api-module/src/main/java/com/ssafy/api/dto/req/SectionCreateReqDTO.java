package com.ssafy.api.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionCreateReqDTO {
    @NotNull
    @ApiModelProperty(value = "게임방 ID", required = true, example = "0")
    private Long gameChannelId;

    @NotNull
    @ApiModelProperty(value = "방장 ID", required = true, example = "0")
    private Long userId;
}

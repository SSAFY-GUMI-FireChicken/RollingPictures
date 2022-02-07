package com.ssafy.api.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameChannelCreateReqDTO {
    @NotNull
    @ApiModelProperty(value = "채널 ID", required = true, example = "0")
    private Long channelId;

    @NotNull
    @ApiModelProperty(value = "방장 ID", required = true, example = "0")
    private Long userId;
}

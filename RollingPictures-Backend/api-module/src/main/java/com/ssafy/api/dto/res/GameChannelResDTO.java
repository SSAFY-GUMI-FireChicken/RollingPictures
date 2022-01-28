package com.ssafy.api.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameChannelResDTO {
    @ApiModelProperty(value = "게임방 id", required = true, example = "1")
    private long id;
}
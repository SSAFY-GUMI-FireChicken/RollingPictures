package com.ssafy.api.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameChannelGetResDTO {
    @ApiModelProperty(value = "게임방 id", required = true, example = "1")
    private Long id;

    @ApiModelProperty(value = "현재 라운드", required = true, example = "1")
    private Integer curRoundNumber;

    @ApiModelProperty(value = "현재 인원", required = true, example = "1")
    private Integer conPeopleCnt;

}

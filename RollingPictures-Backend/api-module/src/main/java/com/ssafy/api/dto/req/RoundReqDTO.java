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

    @ApiModelProperty(value = "입력 기기", required = true, example = "ssaid")
    private String uid;

    @NotNull
    @ApiModelProperty(value = "접근할 Section Host ID", required = true)
    private Long HostId;

    @NotNull
    @ApiModelProperty(value = "게임방 ID", required = true)
    private Long gameChannelId;

}

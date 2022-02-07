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
public class MakeChannelReqDTO {
    @NotNull
    @ApiModelProperty(value = "유저 id", required = true, example = "1")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "방 제목", required = true, example = "1번 방")
    private String title;

    @NotNull
    @ApiModelProperty(value = "유저 id", required = true, example = "Y")
    private YNCode isPublic;

    @NotNull
    @ApiModelProperty(value = "최대 인원", required = true, example = "6")
    private int maxPeopleCnt;
}

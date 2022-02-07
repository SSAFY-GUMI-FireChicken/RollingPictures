package com.ssafy.api.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelListResDTO {
    @ApiModelProperty(value = "총 방 개수")
    long totalCnt;

    @ApiModelProperty(value = "방 목록")
    List<ChannelResDTO> channels;
}

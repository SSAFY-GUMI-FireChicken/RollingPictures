package com.ssafy.api.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelResDTO {
    @ApiModelProperty(value = "방 id", required = true, example = "1")
    private long id;

    @ApiModelProperty(value = "방 코드", example = "X9vqV0")
    private String code;

    @ApiModelProperty(value = "현재 유저 목록")
    private List<UserInfoResDTO> users;

    @ApiModelProperty(value = "방장 아이디", example = "1")
    private Long leaderId;
}

package com.ssafy.api.dto.res;

import com.ssafy.core.code.YNCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResDTO {
    @ApiModelProperty(value = "회원 id", required = true, example = "1")
    private Long id;

    @ApiModelProperty(value = "회원 닉네임", required = true, example = "불닭")
    private String nickname;

    @ApiModelProperty(value = "방장여부", required = true, example = "Y")
    private YNCode isLeader;
}
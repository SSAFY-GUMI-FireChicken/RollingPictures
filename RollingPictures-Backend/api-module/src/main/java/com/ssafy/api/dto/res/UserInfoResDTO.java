package com.ssafy.api.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResDTO {
    @ApiModelProperty(value = "회원 ssaid", required = true, example = "1")
    private String uid;

    @ApiModelProperty(value = "회원 닉네임", required = true, example = "1")
    private String nickname;
}
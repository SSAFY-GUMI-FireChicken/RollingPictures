package com.ssafy.api.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoUpdateReqDTO {
    @NotBlank
    @ApiModelProperty(value = "uid (일반회원:기기정보, sns로그인:uid값)", required = true, example = "kakao123")
    private String uid;

    @ApiModelProperty(value = "닉네임", required = false, example = "카카오")
    private String nickname;


}

package com.ssafy.api.dto.req;

import lombok.*;

import javax.validation.constraints.NotBlank;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoUpdateReqDTO {
    @NotBlank
    private String uid;

    private String nickname;


}

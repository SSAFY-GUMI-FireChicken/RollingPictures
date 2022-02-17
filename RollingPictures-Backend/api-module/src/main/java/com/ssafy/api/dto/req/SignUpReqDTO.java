package com.ssafy.api.dto.req;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpReqDTO {
    @NotBlank
    private String uid;

    @NotNull
    @Pattern(regexp = "^(none|sns)$")
    private String type;

    @NotBlank
    private String password;

    private String nickname;

    
}

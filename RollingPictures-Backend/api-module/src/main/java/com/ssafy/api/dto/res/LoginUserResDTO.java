package com.ssafy.api.dto.res;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserResDTO {
    private long id;

    private String token;
}
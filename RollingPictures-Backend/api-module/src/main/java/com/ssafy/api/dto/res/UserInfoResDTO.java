package com.ssafy.api.dto.res;

import com.ssafy.core.code.YNCode;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResDTO {
    private Long id;

    private String nickname;

    private YNCode isLeader;
}
package com.ssafy.api.dto.res;

import com.ssafy.core.code.YNCode;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelResDTO {
    private long id;

    private String code;

    private String title;

    private YNCode isPublic;

    private int maxPeopleCnt;

    private int curPeopleCnt;

    private List<UserInfoResDTO> users;
}

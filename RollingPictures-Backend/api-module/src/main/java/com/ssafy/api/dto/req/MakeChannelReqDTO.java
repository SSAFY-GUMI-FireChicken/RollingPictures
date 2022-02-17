package com.ssafy.api.dto.req;

import com.ssafy.core.code.YNCode;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MakeChannelReqDTO {
    @NotNull
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private YNCode isPublic;

    @NotNull
    private int maxPeopleCnt;
}

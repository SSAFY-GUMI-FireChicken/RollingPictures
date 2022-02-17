package com.ssafy.api.dto.res;

import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameChannelGetResDTO {
    private Long id;

    private Integer curRoundNumber;

    private Integer conPeopleCnt;

}

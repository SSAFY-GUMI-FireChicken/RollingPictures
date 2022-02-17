package com.ssafy.api.dto.req;

import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameChannelGetReqDTO {
    private String code;
}

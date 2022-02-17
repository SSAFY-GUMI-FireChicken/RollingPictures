package com.ssafy.api.dto.req;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameChannelCreateReqDTO {
    @NotNull
    private Long channelId;

    @NotNull
    private Long userId;
}

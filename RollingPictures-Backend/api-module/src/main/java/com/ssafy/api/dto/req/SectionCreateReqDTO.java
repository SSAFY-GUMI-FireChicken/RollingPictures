package com.ssafy.api.dto.req;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionCreateReqDTO {
    @NotNull
    private Long gameChannelId;

    @NotNull
    private Long userId;
}

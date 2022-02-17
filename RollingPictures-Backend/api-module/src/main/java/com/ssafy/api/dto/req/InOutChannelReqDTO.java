package com.ssafy.api.dto.req;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InOutChannelReqDTO {
    @NotNull
    private Long id;

    @NotNull
    private String code;
}

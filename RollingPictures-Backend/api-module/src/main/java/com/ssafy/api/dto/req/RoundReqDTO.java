package com.ssafy.api.dto.req;

import com.ssafy.core.code.YNCode;
import lombok.*;

import javax.validation.constraints.NotNull;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoundReqDTO {

    private Integer roundNumber;

    private String keyword;

    private Long id;

    @NotNull
    private Long gameChannelId;

}

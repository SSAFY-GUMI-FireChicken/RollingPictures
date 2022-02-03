package com.ssafy.api.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionDeleteReqDTO {
    @NotNull
    @ApiModelProperty(value = "방코드", required = true)
    private String code;


}

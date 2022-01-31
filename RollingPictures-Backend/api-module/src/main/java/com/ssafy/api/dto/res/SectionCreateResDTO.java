package com.ssafy.api.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionCreateResDTO {
    @ApiModelProperty(value = "섹션 id", required = true, example = "1")
    private Long id;
}

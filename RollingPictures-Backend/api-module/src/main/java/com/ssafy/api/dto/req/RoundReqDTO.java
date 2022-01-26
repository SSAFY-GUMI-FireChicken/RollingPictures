package com.ssafy.api.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoundReqDTO {

    @ApiModelProperty(value = "현재 라운드", required = true, example = "2")
    private Integer roundNumber;

    @ApiModelProperty(value = "그림", required = true, example = "amazon.com")
    private MultipartFile file;

}

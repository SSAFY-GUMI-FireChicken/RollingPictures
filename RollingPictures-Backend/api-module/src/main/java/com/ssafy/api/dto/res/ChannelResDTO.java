package com.ssafy.api.dto.res;

import com.ssafy.core.code.YNCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelResDTO {
    @ApiModelProperty(value = "방 id", required = true, example = "1")
    private long id;

    @ApiModelProperty(value = "방 코드", example = "X9vqV0")
    private String code;

    @ApiModelProperty(value = "방 제목")
    private String title;

    @ApiModelProperty(value = "공개방 여부 (Y:공개 N:비공개)")
    private YNCode isPublic;

    @ApiModelProperty(value = "최대 인원")
    private int maxPeopleCnt;

    @ApiModelProperty(value = "현재 인원")
    private int curPeopleCnt;

    @ApiModelProperty(value = "현재 유저 목록")
    private List<UserInfoResDTO> users;
}

package com.ssafy.api.dto.res;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelListResDTO {
    long totalCnt;

    List<ChannelResDTO> channels;
}

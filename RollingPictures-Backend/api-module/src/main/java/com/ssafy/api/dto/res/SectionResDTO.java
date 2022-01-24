package com.ssafy.api.dto.res;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionResDTO {

    private int num;
    private long userId;
    private String img;
    private String keyword;
}

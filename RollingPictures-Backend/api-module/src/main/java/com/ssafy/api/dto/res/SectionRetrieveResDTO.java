package com.ssafy.api.dto.res;

import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionRetrieveResDTO {
    private Long sectionId;
    private Long userId;
    private String nickname;
    private String img;
    private String keyword;
    private Integer roundNum;
}

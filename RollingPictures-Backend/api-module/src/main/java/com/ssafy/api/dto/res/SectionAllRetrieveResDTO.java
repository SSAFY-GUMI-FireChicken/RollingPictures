package com.ssafy.api.dto.res;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionAllRetrieveResDTO {

    private Long sectionId;
    private List<RoundInfo> roundInfos = new ArrayList<>();

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    public static class RoundInfo {
        private String username;
        private String keyword;
        private String img;
        private int roundNumber;
    }
}

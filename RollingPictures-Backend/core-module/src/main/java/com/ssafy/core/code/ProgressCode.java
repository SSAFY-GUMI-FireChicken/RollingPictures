package com.ssafy.core.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProgressCode implements BaseEnumCode<String> {

    NONE("none"),
    NEXT("next"),
    END("end");

    private final String value;
}


package com.ssafy.core.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum GamePlayState implements BaseEnumCode<String> {

    PLAYING("playing"),
    NONE("none");

    private final String value;
}

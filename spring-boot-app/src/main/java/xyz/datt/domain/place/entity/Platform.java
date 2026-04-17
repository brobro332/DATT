package xyz.datt.domain.place.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Platform {
    NAVER("naver", "네이버"),
    GOOGLE("google", "구글");

    private final String code;
    private final String description;

    @JsonCreator
    public static Platform from(String value) {
        for (Platform platform : Platform.values()) {
            if (platform.code.equalsIgnoreCase(value)) {
                return platform;
            }
        }
        return null;
    }
}
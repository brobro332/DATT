package xyz.datt.domain.place.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum PlaceIndustryCategory {
    KOREAN_FOOD("I201", "한식"),
    CHINESE_FOOD("I202", "중식"),
    JAPANESE_FOOD("I203", "일식"),
    WESTERN_FOOD("I204", "서양식"),
    SOUTHEAST_ASIAN_FOOD("I205", "동남아시아"),
    OTHER_FOREIGN_FOOD("I206", "기타 외국"),
    CAFETERIA_BUFFET("I207", "구내식당·뷔페"),
    CATERING("I208", "출장 음식"),
    MOBILE_FOOD("I209", "이동 음식"),
    SIMPLE_FOOD("I210", "기타 간이"),
    BAR("I211", "주점"),
    NON_ALCOHOL("I212", "비알코올"),

    GENERAL_ACCOMMODATION("I101", "일반 숙박"),
    OTHER_ACCOMMODATION("I102", "기타 숙박"),

    CREATIVE_ART("R101", "창작·예술"),
    LIBRARY_HISTORIC_SITE("R102", "도서관·사적지"),
    SPORTS_SERVICE("R103", "스포츠 서비스"),
    AMUSEMENT("R104", "유원지·오락");

    private final String code;
    private final String name;

    public static Optional<PlaceIndustryCategory> fromCode(String code) {
        return Arrays.stream(values())
            .filter(category -> category.code.equals(code))
            .findFirst();
    }
}
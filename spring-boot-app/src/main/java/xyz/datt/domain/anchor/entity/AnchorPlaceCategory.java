package xyz.datt.domain.anchor.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum AnchorPlaceCategory {
    FOOD("맛집", List.of("I201", "I202", "I203", "I204", "I205", "I206", "I207", "I208", "I209", "I210")),
    CAFE("카페", List.of("I212")),
    BAR("술집", List.of("I211")),
    STAY("숙소", List.of("I101", "I102")),
    PLAY("놀거리", List.of("R101", "R102", "R103", "R104"));

    private final String description;
    private final List<String> middleCategoryCodes;
}
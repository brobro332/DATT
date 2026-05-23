package xyz.datt.domain.anchor.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AnchorSortType {
    LATEST("createdAt", "최신순"),
    POPULAR("viewCount", "인기순");

    private final String property;
    private final String description;
}
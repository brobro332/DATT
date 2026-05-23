package xyz.datt.domain.gamification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityType {
    BOOKMARK_ADD("장소 북마크 추가", 5),
    PLACE_REVIEW_CREATE("장소 리뷰 작성", 15),
    ANCHOR_CREATE("Anchor 생성", 30),
    ANCHOR_LIKE_RECEIVED("Anchor 좋아요 획득", 10),
    POPULAR_ANCHOR_SELECTED("인기 Anchor 선정", 50);

    private final String description;
    private final int exp;
}
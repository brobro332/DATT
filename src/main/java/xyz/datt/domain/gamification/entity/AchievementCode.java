package xyz.datt.domain.gamification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AchievementCode {
    FIRST_BOOKMARK("첫 장소 저장"),
    FIRST_REVIEW("첫 리뷰 작성"),
    FIRST_ANCHOR("첫 Anchor 생성"),
    FIRST_ANCHOR_LIKE("첫 Anchor 좋아요 획득");

    private final String description;
}
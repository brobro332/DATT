package xyz.datt.domain.gamification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TitleCode {
    BEGINNER_EXPLORER("초보 탐험가", "DATT에 첫 발을 내디딘 사용자"),
    PLACE_COLLECTOR("장소 수집가", "장소를 꾸준히 저장한 사용자"),
    REVIEW_WRITER("리뷰 기록가", "장소 리뷰를 작성한 사용자"),
    ANCHOR_CREATOR("앵커 크리에이터", "Anchor를 생성한 사용자"),
    LOCAL_HUNTER("로컬 헌터", "지역 기반 탐색을 즐기는 사용자");

    private final String titleName;
    private final String description;
}
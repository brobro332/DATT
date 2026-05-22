package xyz.datt.domain.member.dto;

import xyz.datt.domain.anchor.dto.ProfileAnchorResponse;
import xyz.datt.domain.member.entity.Member;
import xyz.datt.domain.review.dto.ProfileReviewResponse;

import java.util.List;

public record MemberProfileResponse(
    Long memberId,
    String email,
    String nickname,

    int level,
    int exp,
    int requiredExpForNextLevel,

    SelectedTitleResponse selectedTitle,
    int titleCount,
    int achievementCount,

    MemberActivitySummaryResponse activitySummary,

    List<ProfileAnchorResponse> recentAnchors,
    List<ProfileReviewResponse> recentReviews
) {
    public static MemberProfileResponse of(
        Member member,
        int requiredExpForNextLevel,
        SelectedTitleResponse selectedTitle,
        int titleCount,
        int achievementCount,
        MemberActivitySummaryResponse activitySummary,
        List<ProfileAnchorResponse> recentAnchors,
        List<ProfileReviewResponse> recentReviews
    ) {
        return new MemberProfileResponse(
            member.getId(),
            member.getEmail(),
            member.getNickname(),

            member.getLevel(),
            member.getExp(),
            requiredExpForNextLevel,

            selectedTitle,
            titleCount,
            achievementCount,

            activitySummary,

            recentAnchors,
            recentReviews
        );
    }
}
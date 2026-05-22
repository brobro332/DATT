package xyz.datt.domain.member.dto;

public record MemberActivitySummaryResponse(
    long bookmarkCount,
    long reviewCount,
    long anchorCount,
    long receivedLikeCount
) {
}
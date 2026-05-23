package xyz.datt.domain.anchor.dto;

import java.time.LocalDateTime;

public record AnchorPopularResponse(
    Long anchorId,
    String title,

    String basePlaceName,
    String baseAddress,

    Double baseLon,
    Double baseLat,
    Double radiusKm,

    long viewCount,
    long likeCount,
    int placeCount,

    boolean isLiked,

    LocalDateTime createdAt
) {
}
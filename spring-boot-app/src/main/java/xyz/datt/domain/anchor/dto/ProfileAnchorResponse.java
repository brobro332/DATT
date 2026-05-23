package xyz.datt.domain.anchor.dto;

import xyz.datt.domain.anchor.entity.Anchor;

import java.time.LocalDateTime;

public record ProfileAnchorResponse(
    Long anchorId,
    String title,
    String basePlaceName,
    String baseAddress,
    long viewCount,
    LocalDateTime createdAt
) {

    public static ProfileAnchorResponse from(Anchor anchor) {
        return new ProfileAnchorResponse(
            anchor.getId(),
            anchor.getTitle(),
            anchor.getBasePlaceName(),
            anchor.getBaseAddress(),
            anchor.getViewCount(),
            anchor.getCreatedAt()
        );
    }
}
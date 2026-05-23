package xyz.datt.domain.review.dto;

import xyz.datt.domain.review.entity.PlaceReview;

import java.time.LocalDateTime;

public record PlaceReviewResponse(
    Long reviewId,
    Long placeId,
    Long memberId,
    String nickname,
    int rating,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static PlaceReviewResponse from(PlaceReview review) {
        return new PlaceReviewResponse(
            review.getId(),
            review.getPlaceMaster().getId(),
            review.getMember().getId(),
            review.getMember().getNickname(),
            review.getRating(),
            review.getContent(),
            review.getCreatedAt(),
            review.getUpdatedAt()
        );
    }
}
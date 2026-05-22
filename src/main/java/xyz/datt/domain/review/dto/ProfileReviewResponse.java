package xyz.datt.domain.review.dto;

import xyz.datt.domain.review.entity.PlaceReview;

import java.time.LocalDateTime;

public record ProfileReviewResponse(
    Long reviewId,
    Long placeId,
    String placeName,
    int rating,
    String content,
    LocalDateTime createdAt
) {
    public static ProfileReviewResponse from(PlaceReview review) {
        return new ProfileReviewResponse(
            review.getId(),
            review.getPlaceMaster().getId(),
            review.getPlaceMaster().getBizesNm(),
            review.getRating(),
            review.getContent(),
            review.getCreatedAt()
        );
    }
}
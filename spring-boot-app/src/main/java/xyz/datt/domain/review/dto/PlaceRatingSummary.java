package xyz.datt.domain.review.dto;

public record PlaceRatingSummary(
    Double averageRating,
    Long reviewCount
) {
}
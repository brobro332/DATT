package xyz.datt.domain.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import xyz.datt.domain.review.dto.PlaceReviewCreateRequest;
import xyz.datt.domain.review.dto.PlaceReviewResponse;
import xyz.datt.domain.review.dto.PlaceReviewUpdateRequest;
import xyz.datt.domain.review.service.PlaceReviewService;
import xyz.datt.global.response.ApiResponse;
import xyz.datt.global.security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
public class PlaceReviewController {
    private final PlaceReviewService placeReviewService;

    @PostMapping("/api/places/{placeId}/reviews")
    public ApiResponse<PlaceReviewResponse> createReview(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long placeId,
        @Valid @RequestBody PlaceReviewCreateRequest request
    ) {
        PlaceReviewResponse response = placeReviewService.createReview(
            userDetails.getMemberId(),
            placeId,
            request
        );

        return ApiResponse.success(response);
    }

    @PatchMapping("/api/places/{placeId}/reviews/{reviewId}")
    public ApiResponse<PlaceReviewResponse> updateReview(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long placeId,
        @PathVariable Long reviewId,
        @Valid @RequestBody PlaceReviewUpdateRequest request
    ) {
        PlaceReviewResponse response = placeReviewService.updateReview(
            userDetails.getMemberId(),
            placeId,
            reviewId,
            request
        );

        return ApiResponse.success(response);
    }

    @DeleteMapping("/api/places/{placeId}/reviews/{reviewId}")
    public ApiResponse<Void> deleteReview(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long placeId,
        @PathVariable Long reviewId
    ) {
        placeReviewService.deleteReview(
            userDetails.getMemberId(),
            placeId,
            reviewId
        );

        return ApiResponse.success(null);
    }

    @GetMapping("/api/places/{placeId}/reviews")
    public ApiResponse<Page<PlaceReviewResponse>> getPlaceReviews(
        @PathVariable Long placeId,
        Pageable pageable
    ) {
        Page<PlaceReviewResponse> response = placeReviewService.getPlaceReviews(
            placeId,
            pageable
        );

        return ApiResponse.success(response);
    }
}
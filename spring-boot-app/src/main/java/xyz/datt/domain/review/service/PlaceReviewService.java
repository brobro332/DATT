package xyz.datt.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.datt.domain.member.entity.Member;
import xyz.datt.domain.member.repository.MemberRepository;
import xyz.datt.domain.place.entity.PlaceMaster;
import xyz.datt.domain.place.repository.PlaceMasterRepository;
import xyz.datt.domain.review.dto.PlaceRatingSummary;
import xyz.datt.domain.review.dto.PlaceReviewCreateRequest;
import xyz.datt.domain.review.dto.PlaceReviewResponse;
import xyz.datt.domain.review.dto.PlaceReviewUpdateRequest;
import xyz.datt.domain.review.entity.PlaceReview;
import xyz.datt.domain.review.repository.PlaceReviewRepository;
import xyz.datt.global.error.BusinessException;
import xyz.datt.global.error.ErrorCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceReviewService {
    private final PlaceReviewRepository placeReviewRepository;
    private final MemberRepository memberRepository;
    private final PlaceMasterRepository placeMasterRepository;

    @Transactional
    public PlaceReviewResponse createReview(
        Long memberId,
        Long placeId,
        PlaceReviewCreateRequest request
    ) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        PlaceMaster placeMaster = placeMasterRepository.findById(placeId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

        validateNotReviewed(memberId, placeId);

        PlaceReview review = PlaceReview.builder()
            .member(member)
            .placeMaster(placeMaster)
            .rating(request.rating())
            .content(request.content())
            .build();

        PlaceReview savedReview = placeReviewRepository.save(review);

        return PlaceReviewResponse.from(savedReview);
    }

    @Transactional
    public PlaceReviewResponse updateReview(
        Long memberId,
        Long placeId,
        Long reviewId,
        PlaceReviewUpdateRequest request
    ) {
        PlaceReview review = placeReviewRepository.findById(reviewId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_REVIEW_NOT_FOUND));

        validateReviewPlace(review, placeId);
        validateReviewOwner(review, memberId);

        review.update(
            request.rating(),
            request.content()
        );

        return PlaceReviewResponse.from(review);
    }

    @Transactional
    public void deleteReview(
        Long memberId,
        Long placeId,
        Long reviewId
    ) {
        PlaceReview review = placeReviewRepository.findById(reviewId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_REVIEW_NOT_FOUND));

        validateReviewPlace(review, placeId);
        validateReviewOwner(review, memberId);

        placeReviewRepository.delete(review);
    }

    public Page<PlaceReviewResponse> getPlaceReviews(
        Long placeId,
        Pageable pageable
    ) {
        if (!placeMasterRepository.existsById(placeId)) {
            throw new BusinessException(ErrorCode.PLACE_NOT_FOUND);
        }

        return placeReviewRepository
            .findByPlaceMasterIdOrderByCreatedAtDesc(placeId, pageable)
            .map(PlaceReviewResponse::from);
    }

    public PlaceRatingSummary getRatingSummary(Long placeId) {
        if (!placeMasterRepository.existsById(placeId)) {
            throw new BusinessException(ErrorCode.PLACE_NOT_FOUND);
        }

        return placeReviewRepository.getRatingSummaryByPlaceId(placeId);
    }

    private void validateNotReviewed(
        Long memberId,
        Long placeId
    ) {
        if (placeReviewRepository.existsByMemberIdAndPlaceMasterId(memberId, placeId)) {
            throw new BusinessException(ErrorCode.PLACE_REVIEW_ALREADY_EXISTS);
        }
    }

    private void validateReviewPlace(
        PlaceReview review,
        Long placeId
    ) {
        Long reviewPlaceId = review.getPlaceMaster().getId();

        if (placeId != null && reviewPlaceId != null && !reviewPlaceId.equals(placeId)) {
            throw new BusinessException(ErrorCode.PLACE_REVIEW_NOT_FOUND);
        }
    }

    private void validateReviewOwner(
        PlaceReview review,
        Long memberId
    ) {
        Long ownerId = review.getMember().getId();

        if (memberId != null && ownerId != null && !ownerId.equals(memberId)) {
            throw new BusinessException(ErrorCode.PLACE_REVIEW_ACCESS_DENIED);
        }
    }
}
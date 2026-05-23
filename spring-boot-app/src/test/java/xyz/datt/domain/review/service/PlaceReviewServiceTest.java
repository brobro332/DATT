package xyz.datt.domain.review.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import xyz.datt.domain.member.entity.Member;
import xyz.datt.domain.member.repository.MemberRepository;
import xyz.datt.domain.place.entity.PlaceMaster;
import xyz.datt.domain.place.repository.PlaceMasterRepository;
import xyz.datt.domain.review.dto.*;
import xyz.datt.domain.review.entity.PlaceReview;
import xyz.datt.domain.review.repository.PlaceReviewRepository;
import xyz.datt.global.error.BusinessException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaceReviewServiceTest {
    private final PlaceReviewRepository placeReviewRepository = mock(PlaceReviewRepository.class);
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final PlaceMasterRepository placeMasterRepository = mock(PlaceMasterRepository.class);

    private final PlaceReviewService placeReviewService = new PlaceReviewService(
        placeReviewRepository,
        memberRepository,
        placeMasterRepository
    );

    @Test
    @DisplayName("장소 리뷰를 작성한다.")
    void givenValidRequest_whenCreateReview_thenReturnReview() {
        Member member = createMember();
        PlaceMaster place = createPlace();

        PlaceReview review = PlaceReview.builder()
            .member(member)
            .placeMaster(place)
            .rating(5)
            .content("좋았습니다.")
            .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(placeMasterRepository.findById(10L)).thenReturn(Optional.of(place));
        when(placeReviewRepository.existsByMemberIdAndPlaceMasterId(1L, 10L)).thenReturn(false);
        when(placeReviewRepository.save(any(PlaceReview.class))).thenReturn(review);

        PlaceReviewCreateRequest request = new PlaceReviewCreateRequest(5, "좋았습니다.");

        PlaceReviewResponse response = placeReviewService.createReview(1L, 10L, request);

        assertThat(response.rating()).isEqualTo(5);
        assertThat(response.content()).isEqualTo("좋았습니다.");

        verify(placeReviewRepository).save(any(PlaceReview.class));
    }

    @Test
    @DisplayName("이미 리뷰를 작성한 장소면 예외가 발생한다.")
    void givenDuplicatedReview_whenCreateReview_thenThrowException() {
        Member member = createMember();
        PlaceMaster place = createPlace();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(placeMasterRepository.findById(10L)).thenReturn(Optional.of(place));
        when(placeReviewRepository.existsByMemberIdAndPlaceMasterId(1L, 10L)).thenReturn(true);

        PlaceReviewCreateRequest request = new PlaceReviewCreateRequest(4, "괜찮았습니다.");

        assertThatThrownBy(() -> placeReviewService.createReview(1L, 10L, request))
            .isInstanceOf(BusinessException.class);

        verify(placeReviewRepository, never()).save(any());
    }

    @Test
    @DisplayName("장소 리뷰를 수정한다.")
    void givenOwner_whenUpdateReview_thenReturnUpdatedReview() {
        Member member = createMember();
        PlaceMaster place = createPlace();

        PlaceReview review = PlaceReview.builder()
            .member(member)
            .placeMaster(place)
            .rating(3)
            .content("보통입니다.")
            .build();

        when(placeReviewRepository.findById(100L)).thenReturn(Optional.of(review));

        PlaceReviewUpdateRequest request = new PlaceReviewUpdateRequest(5, "수정 후 좋았습니다.");

        PlaceReviewResponse response = placeReviewService.updateReview(
            null,
            null,
            100L,
            request
        );

        assertThat(response.rating()).isEqualTo(5);
        assertThat(response.content()).isEqualTo("수정 후 좋았습니다.");
    }

    @Test
    @DisplayName("장소 리뷰를 삭제한다.")
    void givenOwner_whenDeleteReview_thenDeleteReview() {
        PlaceReview review = PlaceReview.builder()
            .member(createMember())
            .placeMaster(createPlace())
            .rating(4)
            .content("좋아요.")
            .build();

        when(placeReviewRepository.findById(100L)).thenReturn(Optional.of(review));

        placeReviewService.deleteReview(null, null, 100L);

        verify(placeReviewRepository).delete(review);
    }

    @Test
    @DisplayName("장소 리뷰 목록을 조회한다.")
    void givenPlaceId_whenGetPlaceReviews_thenReturnReviews() {
        PlaceReview review = PlaceReview.builder()
            .member(createMember())
            .placeMaster(createPlace())
            .rating(5)
            .content("좋았습니다.")
            .build();

        Pageable pageable = PageRequest.of(0, 10);
        Page<PlaceReview> page = new PageImpl<>(List.of(review), pageable, 1);

        when(placeMasterRepository.existsById(10L)).thenReturn(true);
        when(placeReviewRepository.findByPlaceMasterIdOrderByCreatedAtDesc(10L, pageable))
            .thenReturn(page);

        Page<PlaceReviewResponse> result = placeReviewService.getPlaceReviews(10L, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).rating()).isEqualTo(5);
    }

    @Test
    @DisplayName("장소 평점 집계를 조회한다.")
    void givenPlaceId_whenGetRatingSummary_thenReturnSummary() {
        PlaceRatingSummary summary = new PlaceRatingSummary(4.5, 2L);

        when(placeMasterRepository.existsById(10L)).thenReturn(true);
        when(placeReviewRepository.getRatingSummaryByPlaceId(10L)).thenReturn(summary);

        PlaceRatingSummary result = placeReviewService.getRatingSummary(10L);

        assertThat(result.averageRating()).isEqualTo(4.5);
        assertThat(result.reviewCount()).isEqualTo(2L);
    }

    private Member createMember() {
        return Member.createUser(
            "test@test.com",
            "encodedPassword",
            "bro"
        );
    }

    private PlaceMaster createPlace() {
        return PlaceMaster.builder()
            .bizesId("BIZ-001")
            .bizesNm("스타벅스 성수점")
            .brchNm("성수점")
            .indsMclsCd("I212")
            .indsMclsNm("비알코올")
            .rdnmAdr("서울특별시 성동구 성수동")
            .lon(127.0560)
            .lat(37.5450)
            .location("POINT(127.0560 37.5450)")
            .build();
    }
}
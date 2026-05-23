package xyz.datt.domain.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.datt.domain.review.dto.PlaceRatingSummary;
import xyz.datt.domain.review.entity.PlaceReview;

import java.util.List;

public interface PlaceReviewRepository extends JpaRepository<PlaceReview, Long> {
    boolean existsByMemberIdAndPlaceMasterId(Long memberId, Long placeId);
    Page<PlaceReview> findByPlaceMasterIdOrderByCreatedAtDesc(Long placeId, Pageable pageable);
    @Query("""
        select new xyz.datt.domain.review.dto.PlaceRatingSummary(
            coalesce(avg(r.rating), 0.0),
            count(r)
        )
        from PlaceReview r
        where r.placeMaster.id = :placeId
    """)
    PlaceRatingSummary getRatingSummaryByPlaceId(@Param("placeId") Long placeId);

    long countByMemberId(Long memberId);
    List<PlaceReview> findTop3ByMemberIdOrderByCreatedAtDesc(Long memberId);
}
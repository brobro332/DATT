package xyz.datt.domain.review.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.datt.domain.member.entity.Member;
import xyz.datt.domain.place.entity.PlaceMaster;
import xyz.datt.global.entity.BaseEntity;
import xyz.datt.global.error.BusinessException;
import xyz.datt.global.error.ErrorCode;

@Getter
@Entity
@Table(
    name = "place_review",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_place_review_member_place",
            columnNames = {"member_id", "place_id"}
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceReview extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "place_id", nullable = false)
    private PlaceMaster placeMaster;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false, length = 1000)
    private String content;

    @Builder
    private PlaceReview(
        Member member,
        PlaceMaster placeMaster,
        int rating,
        String content
    ) {
        validateRating(rating);

        this.member = member;
        this.placeMaster = placeMaster;
        this.rating = rating;
        this.content = content;
    }

    public void update(
        int rating,
        String content
    ) {
        validateRating(rating);

        this.rating = rating;
        this.content = content;
    }

    private void validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new BusinessException(ErrorCode.INVALID_REVIEW_RATING);
        }
    }
}
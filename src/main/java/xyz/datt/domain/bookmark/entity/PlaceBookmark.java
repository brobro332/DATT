package xyz.datt.domain.bookmark.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.datt.domain.member.entity.Member;
import xyz.datt.domain.place.entity.PlaceMaster;
import xyz.datt.global.entity.BaseEntity;

@Getter
@Entity
@Table(
    name = "place_bookmarks",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_place_bookmark_member_place",
            columnNames = {"member_id", "place_id"}
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceBookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "place_id", nullable = false)
    private PlaceMaster placeMaster;

    @Builder
    private PlaceBookmark(Member member, PlaceMaster placeMaster) {
        this.member = member;
        this.placeMaster = placeMaster;
    }
}
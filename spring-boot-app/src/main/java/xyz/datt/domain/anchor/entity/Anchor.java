package xyz.datt.domain.anchor.entity;

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
@Table(name = "anchor")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Anchor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_place_id")
    private PlaceMaster basePlace;

    @Column(length = 100)
    private String basePlaceName;

    @Column(length = 255)
    private String baseAddress;

    @Column(nullable = false)
    private Double baseLon;

    @Column(nullable = false)
    private Double baseLat;

    @Column(nullable = false)
    private Double radiusKm;

    @Column(nullable = false)
    private boolean isPublic;

    @Column(nullable = false)
    private long viewCount;

    @Builder
    private Anchor(
        Member member,
        String title,
        PlaceMaster basePlace,
        String basePlaceName,
        String baseAddress,
        Double baseLon,
        Double baseLat,
        Double radiusKm,
        boolean isPublic
    ) {
        this.member = member;
        this.title = title;
        this.basePlace = basePlace;
        this.basePlaceName = basePlaceName;
        this.baseAddress = baseAddress;
        this.baseLon = baseLon;
        this.baseLat = baseLat;
        this.radiusKm = radiusKm == null ? 3.0 : radiusKm;
        this.isPublic = isPublic;
        this.viewCount = 0L;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void changeVisibility(boolean isPublic) {
        this.isPublic = isPublic;
    }
}
package xyz.datt.domain.anchor.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.datt.domain.place.entity.PlaceMaster;
import xyz.datt.global.entity.BaseEntity;

@Getter
@Entity
@Table(name = "anchor_place")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnchorPlace extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "anchor_id", nullable = false)
    private Anchor anchor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "place_id", nullable = false)
    private PlaceMaster placeMaster;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AnchorPlaceCategory category;

    @Column(nullable = false)
    private Double distanceKm;

    @Column(nullable = false)
    private int recommendOrder;

    @Builder
    private AnchorPlace(
        Anchor anchor,
        PlaceMaster placeMaster,
        AnchorPlaceCategory category,
        Double distanceKm,
        int recommendOrder
    ) {
        this.anchor = anchor;
        this.placeMaster = placeMaster;
        this.category = category;
        this.distanceKm = distanceKm;
        this.recommendOrder = recommendOrder;
    }
}
package xyz.datt.domain.anchor.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.datt.domain.member.entity.Member;
import xyz.datt.global.entity.BaseEntity;

@Getter
@Entity
@Table(
    name = "anchor_like",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_anchor_like_member_anchor",
            columnNames = {"member_id", "anchor_id"}
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnchorLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "anchor_id", nullable = false)
    private Anchor anchor;

    @Builder
    private AnchorLike(
        Member member,
        Anchor anchor
    ) {
        this.member = member;
        this.anchor = anchor;
    }
}
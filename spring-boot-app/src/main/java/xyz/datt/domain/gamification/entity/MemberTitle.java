package xyz.datt.domain.gamification.entity;

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
    name = "member_title",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_member_title_member_title",
            columnNames = {"member_id", "title_id"}
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTitle extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "title_id", nullable = false)
    private Title title;

    @Column(nullable = false)
    private boolean selected;

    @Builder
    private MemberTitle(
        Member member,
        Title title,
        boolean selected
    ) {
        this.member = member;
        this.title = title;
        this.selected = selected;
    }

    public void select() {
        this.selected = true;
    }

    public void unselect() {
        this.selected = false;
    }
}
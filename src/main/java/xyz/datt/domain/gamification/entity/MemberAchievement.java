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
    name = "member_achievement",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_member_achievement_member_achievement",
            columnNames = {"member_id", "achievement_id"}
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAchievement extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievement;

    @Builder
    private MemberAchievement(
        Member member,
        Achievement achievement
    ) {
        this.member = member;
        this.achievement = achievement;
    }
}
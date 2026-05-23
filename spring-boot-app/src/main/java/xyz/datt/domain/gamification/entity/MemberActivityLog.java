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
@Table(name = "member_activity_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberActivityLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ActivityType activityType;

    @Column(nullable = false)
    private int expAmount;

    @Column(nullable = false, length = 255)
    private String description;

    @Builder
    private MemberActivityLog(
        Member member,
        ActivityType activityType,
        int expAmount,
        String description
    ) {
        this.member = member;
        this.activityType = activityType;
        this.expAmount = expAmount;
        this.description = description;
    }
}
package xyz.datt.domain.gamification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.datt.domain.gamification.entity.MemberAchievement;

public interface MemberAchievementRepository extends JpaRepository<MemberAchievement, Long> {
    boolean existsByMemberIdAndAchievementCode(Long memberId, String achievementCode);
    int countByMemberId(Long memberId);
}
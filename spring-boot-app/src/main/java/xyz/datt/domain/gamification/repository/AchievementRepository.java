package xyz.datt.domain.gamification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.datt.domain.gamification.entity.Achievement;

import java.util.Optional;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Optional<Achievement> findByCode(String code);
}
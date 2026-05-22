package xyz.datt.domain.gamification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.datt.domain.gamification.entity.Title;

import java.util.Optional;

public interface TitleRepository extends JpaRepository<Title, Long> {
    Optional<Title> findByCode(String code);
}
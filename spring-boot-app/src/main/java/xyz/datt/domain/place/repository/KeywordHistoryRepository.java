package xyz.datt.domain.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.datt.domain.place.entity.KeywordHistory;
import xyz.datt.domain.place.entity.Platform;

import java.util.Optional;

public interface KeywordHistoryRepository extends JpaRepository<KeywordHistory, Long> {
    Optional<KeywordHistory> findByKeywordAndCategoryAndPlatform(String keyword, String category, Platform platform);
}

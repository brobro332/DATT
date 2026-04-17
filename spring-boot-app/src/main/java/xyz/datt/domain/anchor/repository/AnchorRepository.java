package xyz.datt.domain.anchor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.datt.domain.anchor.entity.AnchorSnapshot;

import java.util.Optional;

public interface AnchorRepository extends JpaRepository<AnchorSnapshot, String> {
    Optional<AnchorSnapshot> findFirstByKeywordOrderByCreatedAtDesc(String keyword);
}

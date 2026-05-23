package xyz.datt.domain.anchor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.datt.domain.anchor.entity.AnchorPlace;

import java.util.List;

public interface AnchorPlaceRepository extends JpaRepository<AnchorPlace, Long> {
    List<AnchorPlace> findByAnchorIdOrderByCategoryAscRecommendOrderAsc(Long anchorId);
    int countByAnchorId(Long anchorId);
}
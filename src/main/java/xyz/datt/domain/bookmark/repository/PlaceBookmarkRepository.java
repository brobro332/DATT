package xyz.datt.domain.bookmark.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.datt.domain.bookmark.entity.PlaceBookmark;

import java.util.Optional;

public interface PlaceBookmarkRepository extends JpaRepository<PlaceBookmark, Long> {
    boolean existsByMemberIdAndPlaceMasterId(Long memberId, Long placeId);
    Optional<PlaceBookmark> findByMemberIdAndPlaceMasterId(Long memberId, Long placeId);
    Page<PlaceBookmark> findByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);
}
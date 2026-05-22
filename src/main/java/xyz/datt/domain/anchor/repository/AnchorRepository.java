package xyz.datt.domain.anchor.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import xyz.datt.domain.anchor.entity.Anchor;

import java.util.List;

public interface AnchorRepository extends JpaRepository<Anchor, Long> {
    Page<Anchor> findByIsPublicTrueOrderByCreatedAtDesc(Pageable pageable);
    Page<Anchor> findByIsPublicTrueOrderByViewCountDesc(Pageable pageable);
    Page<Anchor> findByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);
    Page<Anchor> findByMemberIdOrderByViewCountDesc(Long memberId, Pageable pageable);
    @Query("""
        select a
        from Anchor a
        left join AnchorLike al on al.anchor = a
        where a.isPublic = true
        group by a
        order by a.viewCount desc, count(al) desc, a.createdAt desc
    """)
    Page<Anchor> findPopularAnchors(Pageable pageable);

    long countByMemberId(Long memberId);
    List<Anchor> findTop3ByMemberIdOrderByCreatedAtDesc(Long memberId);
}
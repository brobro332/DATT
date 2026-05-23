package xyz.datt.domain.anchor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.datt.domain.anchor.entity.AnchorLike;

import java.util.Optional;

public interface AnchorLikeRepository extends JpaRepository<AnchorLike, Long> {
    boolean existsByMemberIdAndAnchorId(Long memberId, Long anchorId);
    Optional<AnchorLike> findByMemberIdAndAnchorId(Long memberId, Long anchorId);
    int countByAnchorId(Long anchorId);

    @Query("""
        select count(al)
        from AnchorLike al
        where al.anchor.member.id = :memberId
    """)
    long countReceivedLikesByMemberId(@Param("memberId") Long memberId);
}
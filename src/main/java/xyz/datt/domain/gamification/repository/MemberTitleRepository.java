package xyz.datt.domain.gamification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.datt.domain.gamification.entity.MemberTitle;

import java.util.List;
import java.util.Optional;

public interface MemberTitleRepository extends JpaRepository<MemberTitle, Long> {
    boolean existsByMemberIdAndTitleCode(
        Long memberId,
        String titleCode
    );

    List<MemberTitle> findByMemberId(
        Long memberId
    );

    Optional<MemberTitle> findByMemberIdAndSelectedTrue(
        Long memberId
    );

    Optional<MemberTitle> findByMemberIdAndTitleId(
        Long memberId,
        Long titleId
    );
}
package xyz.datt.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.datt.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<Member> findByEmail(String email);
}
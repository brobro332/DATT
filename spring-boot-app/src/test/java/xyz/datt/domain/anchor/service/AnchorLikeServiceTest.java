package xyz.datt.domain.anchor.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.datt.domain.anchor.entity.Anchor;
import xyz.datt.domain.anchor.entity.AnchorLike;
import xyz.datt.domain.anchor.repository.AnchorLikeRepository;
import xyz.datt.domain.anchor.repository.AnchorRepository;
import xyz.datt.domain.member.entity.Member;
import xyz.datt.domain.member.repository.MemberRepository;
import xyz.datt.global.error.BusinessException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnchorLikeServiceTest {
    private final AnchorLikeRepository anchorLikeRepository = mock(AnchorLikeRepository.class);
    private final AnchorRepository anchorRepository = mock(AnchorRepository.class);
    private final MemberRepository memberRepository = mock(MemberRepository.class);

    private final AnchorLikeService anchorLikeService = new AnchorLikeService(
        anchorLikeRepository,
        anchorRepository,
        memberRepository
    );

    @Test
    @DisplayName("Anchor 좋아요를 추가한다.")
    void givenMemberAndAnchor_whenLikeAnchor_thenSaveLike() {
        Member member = createMember();
        Anchor anchor = createAnchor(member);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(anchorRepository.findById(10L)).thenReturn(Optional.of(anchor));
        when(anchorLikeRepository.existsByMemberIdAndAnchorId(1L, 10L)).thenReturn(false);

        anchorLikeService.likeAnchor(1L, 10L);

        verify(anchorLikeRepository).save(any(AnchorLike.class));
    }

    @Test
    @DisplayName("이미 좋아요한 Anchor면 예외가 발생한다.")
    void givenDuplicatedLike_whenLikeAnchor_thenThrowException() {
        Member member = createMember();
        Anchor anchor = createAnchor(member);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(anchorRepository.findById(10L)).thenReturn(Optional.of(anchor));
        when(anchorLikeRepository.existsByMemberIdAndAnchorId(1L, 10L)).thenReturn(true);

        assertThatThrownBy(() -> anchorLikeService.likeAnchor(1L, 10L))
            .isInstanceOf(BusinessException.class);

        verify(anchorLikeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Anchor 좋아요를 삭제한다.")
    void givenLikedAnchor_whenUnlikeAnchor_thenDeleteLike() {
        Member member = createMember();
        Anchor anchor = createAnchor(member);

        AnchorLike anchorLike = AnchorLike.builder()
            .member(member)
            .anchor(anchor)
            .build();

        when(anchorLikeRepository.findByMemberIdAndAnchorId(1L, 10L))
            .thenReturn(Optional.of(anchorLike));

        anchorLikeService.unlikeAnchor(1L, 10L);

        verify(anchorLikeRepository).delete(anchorLike);
    }

    @Test
    @DisplayName("좋아요하지 않은 Anchor를 삭제하면 예외가 발생한다.")
    void givenNotLikedAnchor_whenUnlikeAnchor_thenThrowException() {
        when(anchorLikeRepository.findByMemberIdAndAnchorId(1L, 10L))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> anchorLikeService.unlikeAnchor(1L, 10L))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("Anchor 좋아요 여부를 조회한다.")
    void givenMemberAndAnchor_whenIsLiked_thenReturnTrue() {
        when(anchorLikeRepository.existsByMemberIdAndAnchorId(1L, 10L))
            .thenReturn(true);

        boolean result = anchorLikeService.isLiked(1L, 10L);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Anchor 좋아요 수를 조회한다.")
    void givenAnchor_whenCountLikes_thenReturnCount() {
        when(anchorLikeRepository.countByAnchorId(10L))
            .thenReturn(3);

        int result = anchorLikeService.countLikes(10L);

        assertThat(result).isEqualTo(3);
    }

    private Member createMember() {
        return Member.createUser(
            "test@test.com",
            "encodedPassword",
            "bro"
        );
    }

    private Anchor createAnchor(Member member) {
        return Anchor.builder()
            .member(member)
            .title("성수 감성 데이트")
            .basePlaceName("성수역")
            .baseAddress("서울특별시 성동구 성수동")
            .baseLon(127.0557)
            .baseLat(37.5446)
            .radiusKm(3.0)
            .isPublic(true)
            .build();
    }
}
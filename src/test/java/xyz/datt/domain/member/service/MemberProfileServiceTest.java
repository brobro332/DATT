package xyz.datt.domain.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.datt.domain.anchor.repository.AnchorLikeRepository;
import xyz.datt.domain.anchor.repository.AnchorRepository;
import xyz.datt.domain.bookmark.repository.PlaceBookmarkRepository;
import xyz.datt.domain.gamification.repository.MemberAchievementRepository;
import xyz.datt.domain.gamification.repository.MemberTitleRepository;
import xyz.datt.domain.member.dto.MemberProfileResponse;
import xyz.datt.domain.member.entity.Member;
import xyz.datt.domain.member.repository.MemberRepository;
import xyz.datt.domain.review.repository.PlaceReviewRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MemberProfileServiceTest {
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final MemberAchievementRepository memberAchievementRepository = mock(MemberAchievementRepository.class);
    private final MemberTitleRepository memberTitleRepository = mock(MemberTitleRepository.class);
    private final PlaceBookmarkRepository placeBookmarkRepository = mock(PlaceBookmarkRepository.class);
    private final PlaceReviewRepository placeReviewRepository = mock(PlaceReviewRepository.class);
    private final AnchorRepository anchorRepository = mock(AnchorRepository.class);
    private final AnchorLikeRepository anchorLikeRepository = mock(AnchorLikeRepository.class);

    private final MemberProfileService memberProfileService = new MemberProfileService(
        memberRepository,
        memberAchievementRepository,
        memberTitleRepository,
        placeBookmarkRepository,
        placeReviewRepository,
        anchorRepository,
        anchorLikeRepository
    );

    @Test
    @DisplayName("내 프로필 성장 정보를 조회한다.")
    void givenMemberId_whenGetMyProfile_thenReturnProfile() {
        Member member = Member.createUser(
            "test@test.com",
            "encodedPassword",
            "bro"
        );

        member.addExp(130);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberTitleRepository.findByMemberIdAndSelectedTrue(1L)).thenReturn(Optional.empty());
        when(memberTitleRepository.findByMemberId(1L)).thenReturn(List.of());
        when(memberAchievementRepository.countByMemberId(1L)).thenReturn(2);
        when(placeBookmarkRepository.countByMemberId(1L)).thenReturn(3L);
        when(placeReviewRepository.countByMemberId(1L)).thenReturn(4L);
        when(anchorRepository.countByMemberId(1L)).thenReturn(5L);
        when(anchorLikeRepository.countReceivedLikesByMemberId(1L)).thenReturn(6L);        when(anchorRepository.findTop3ByMemberIdOrderByCreatedAtDesc(1L)).thenReturn(List.of());
        when(placeReviewRepository.findTop3ByMemberIdOrderByCreatedAtDesc(1L)).thenReturn(List.of());

        MemberProfileResponse response = memberProfileService.getMyProfile(1L);

        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo("test@test.com");
        assertThat(response.nickname()).isEqualTo("bro");
        assertThat(response.level()).isEqualTo(2);
        assertThat(response.exp()).isEqualTo(130);
        assertThat(response.achievementCount()).isEqualTo(2);
        assertThat(response.activitySummary().bookmarkCount()).isEqualTo(3L);
        assertThat(response.activitySummary().reviewCount()).isEqualTo(4L);
        assertThat(response.activitySummary().anchorCount()).isEqualTo(5L);
        assertThat(response.activitySummary().receivedLikeCount()).isEqualTo(6L);
    }
}
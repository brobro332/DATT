package xyz.datt.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.datt.domain.anchor.dto.ProfileAnchorResponse;
import xyz.datt.domain.anchor.repository.AnchorLikeRepository;
import xyz.datt.domain.anchor.repository.AnchorRepository;
import xyz.datt.domain.bookmark.repository.PlaceBookmarkRepository;
import xyz.datt.domain.gamification.entity.MemberTitle;
import xyz.datt.domain.gamification.policy.LevelPolicy;
import xyz.datt.domain.gamification.repository.MemberAchievementRepository;
import xyz.datt.domain.gamification.repository.MemberTitleRepository;
import xyz.datt.domain.member.dto.MemberActivitySummaryResponse;
import xyz.datt.domain.member.dto.MemberProfileResponse;
import xyz.datt.domain.member.dto.SelectedTitleResponse;
import xyz.datt.domain.member.entity.Member;
import xyz.datt.domain.member.repository.MemberRepository;
import xyz.datt.domain.review.dto.ProfileReviewResponse;
import xyz.datt.domain.review.repository.PlaceReviewRepository;
import xyz.datt.global.error.BusinessException;
import xyz.datt.global.error.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberProfileService {

    private final MemberRepository memberRepository;
    private final MemberAchievementRepository memberAchievementRepository;
    private final MemberTitleRepository memberTitleRepository;
    private final PlaceBookmarkRepository placeBookmarkRepository;
    private final PlaceReviewRepository placeReviewRepository;
    private final AnchorRepository anchorRepository;
    private final AnchorLikeRepository anchorLikeRepository;

    public MemberProfileResponse getMyProfile(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        MemberTitle selectedTitle = memberTitleRepository
            .findByMemberIdAndSelectedTrue(memberId)
            .orElse(null);

        int titleCount = memberTitleRepository.findByMemberId(memberId).size();
        int achievementCount = memberAchievementRepository.countByMemberId(memberId);

        long bookmarkCount = placeBookmarkRepository.countByMemberId(memberId);
        long reviewCount = placeReviewRepository.countByMemberId(memberId);
        long anchorCount = anchorRepository.countByMemberId(memberId);
        long receivedLikeCount = anchorLikeRepository.countReceivedLikesByMemberId(memberId);

        List<ProfileAnchorResponse> recentAnchors = anchorRepository
            .findTop3ByMemberIdOrderByCreatedAtDesc(memberId)
            .stream()
            .map(ProfileAnchorResponse::from)
            .toList();

        List<ProfileReviewResponse> recentReviews = placeReviewRepository
            .findTop3ByMemberIdOrderByCreatedAtDesc(memberId)
            .stream()
            .map(ProfileReviewResponse::from)
            .toList();

        return MemberProfileResponse.of(
            member,
            LevelPolicy.getRequiredExpForNextLevel(member.getLevel()),
            SelectedTitleResponse.from(selectedTitle),
            titleCount,
            achievementCount,
            new MemberActivitySummaryResponse(
                bookmarkCount,
                reviewCount,
                anchorCount,
                receivedLikeCount
            ),
            recentAnchors,
            recentReviews
        );
    }
}
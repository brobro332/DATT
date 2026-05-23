package xyz.datt.domain.anchor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.datt.domain.anchor.entity.Anchor;
import xyz.datt.domain.anchor.entity.AnchorLike;
import xyz.datt.domain.anchor.repository.AnchorLikeRepository;
import xyz.datt.domain.anchor.repository.AnchorRepository;
import xyz.datt.domain.member.entity.Member;
import xyz.datt.domain.member.repository.MemberRepository;
import xyz.datt.global.error.BusinessException;
import xyz.datt.global.error.ErrorCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnchorLikeService {
    private final AnchorLikeRepository anchorLikeRepository;
    private final AnchorRepository anchorRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void likeAnchor(Long memberId, Long anchorId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Anchor anchor = anchorRepository.findById(anchorId)
            .orElseThrow(() -> new BusinessException(ErrorCode.ANCHOR_NOT_FOUND));

        if (anchorLikeRepository.existsByMemberIdAndAnchorId(memberId, anchorId)) {
            throw new BusinessException(ErrorCode.ANCHOR_LIKE_ALREADY_EXISTS);
        }

        AnchorLike anchorLike = AnchorLike.builder()
            .member(member)
            .anchor(anchor)
            .build();

        anchorLikeRepository.save(anchorLike);
    }

    @Transactional
    public void unlikeAnchor(Long memberId, Long anchorId) {
        AnchorLike anchorLike = anchorLikeRepository
            .findByMemberIdAndAnchorId(memberId, anchorId)
            .orElseThrow(() -> new BusinessException(ErrorCode.ANCHOR_LIKE_NOT_FOUND));

        anchorLikeRepository.delete(anchorLike);
    }

    public boolean isLiked(Long memberId, Long anchorId) {
        return anchorLikeRepository.existsByMemberIdAndAnchorId(memberId, anchorId);
    }

    public int countLikes(Long anchorId) {
        return anchorLikeRepository.countByAnchorId(anchorId);
    }
}
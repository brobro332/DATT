package xyz.datt.domain.bookmark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.datt.domain.bookmark.dto.PlaceBookmarkResponse;
import xyz.datt.domain.bookmark.entity.PlaceBookmark;
import xyz.datt.domain.bookmark.repository.PlaceBookmarkRepository;
import xyz.datt.domain.member.entity.Member;
import xyz.datt.domain.member.repository.MemberRepository;
import xyz.datt.domain.place.entity.PlaceMaster;
import xyz.datt.domain.place.repository.PlaceMasterRepository;
import xyz.datt.global.error.BusinessException;
import xyz.datt.global.error.ErrorCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceBookmarkService {
    private final PlaceBookmarkRepository placeBookmarkRepository;
    private final MemberRepository memberRepository;
    private final PlaceMasterRepository placeMasterRepository;

    @Transactional
    public PlaceBookmarkResponse addBookmark(
        Long memberId,
        Long placeId
    ) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        PlaceMaster placeMaster = placeMasterRepository.findById(placeId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

        validateNotBookmarked(memberId, placeId);

        PlaceBookmark placeBookmark = PlaceBookmark.builder()
            .member(member)
            .placeMaster(placeMaster)
            .build();

        PlaceBookmark savedBookmark = placeBookmarkRepository.save(placeBookmark);

        return PlaceBookmarkResponse.from(savedBookmark);
    }

    @Transactional
    public void removeBookmark(
        Long memberId,
        Long placeId
    ) {
        PlaceBookmark placeBookmark = placeBookmarkRepository
            .findByMemberIdAndPlaceMasterId(memberId, placeId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_BOOKMARK_NOT_FOUND));

        placeBookmarkRepository.delete(placeBookmark);
    }

    public Page<PlaceBookmarkResponse> getMyBookmarks(
        Long memberId,
        Pageable pageable
    ) {
        return placeBookmarkRepository.findByMemberIdOrderByCreatedAtDesc(memberId, pageable)
            .map(PlaceBookmarkResponse::from);
    }

    public boolean isBookmarked(
        Long memberId,
        Long placeId
    ) {
        return placeBookmarkRepository.existsByMemberIdAndPlaceMasterId(memberId, placeId);
    }

    private void validateNotBookmarked(
        Long memberId,
        Long placeId
    ) {
        if (placeBookmarkRepository.existsByMemberIdAndPlaceMasterId(memberId, placeId)) {
            throw new BusinessException(ErrorCode.PLACE_BOOKMARK_ALREADY_EXISTS);
        }
    }
}
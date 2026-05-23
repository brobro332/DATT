package xyz.datt.domain.anchor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.datt.domain.anchor.dto.AnchorDetailResponse;
import xyz.datt.domain.anchor.dto.AnchorPlaceGroupResponse;
import xyz.datt.domain.anchor.dto.AnchorPlaceResponse;
import xyz.datt.domain.anchor.entity.Anchor;
import xyz.datt.domain.anchor.entity.AnchorPlace;
import xyz.datt.domain.anchor.entity.AnchorPlaceCategory;
import xyz.datt.domain.anchor.repository.AnchorPlaceRepository;
import xyz.datt.domain.anchor.repository.AnchorRepository;
import xyz.datt.global.error.BusinessException;
import xyz.datt.global.error.ErrorCode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnchorDetailService {
    private final AnchorRepository anchorRepository;
    private final AnchorPlaceRepository anchorPlaceRepository;
    private final AnchorLikeService anchorLikeService;

    @Transactional
    public AnchorDetailResponse getAnchorDetail(
        Long memberId,
        Long anchorId
    ) {
        Anchor anchor = anchorRepository.findById(anchorId)
            .orElseThrow(() -> new BusinessException(ErrorCode.ANCHOR_NOT_FOUND));

        validateReadable(anchor, memberId);

        anchor.increaseViewCount();

        List<AnchorPlace> anchorPlaces =
                anchorPlaceRepository.findByAnchorIdOrderByCategoryAscRecommendOrderAsc(anchorId);

        List<AnchorPlaceGroupResponse> placeGroups = groupByCategory(anchorPlaces);

        int likeCount = anchorLikeService.countLikes(anchorId);

        boolean isLiked = memberId != null
            && anchorLikeService.isLiked(memberId, anchorId);

        return AnchorDetailResponse.of(
            anchor,
            likeCount,
            isLiked,
            placeGroups
        );
    }

    private void validateReadable(
        Anchor anchor,
        Long memberId
    ) {
        if (anchor.isPublic()) {
            return;
        }

        Long ownerId = anchor.getMember().getId();

        if (memberId == null || ownerId == null || !ownerId.equals(memberId)) {
            throw new BusinessException(ErrorCode.ANCHOR_ACCESS_DENIED);
        }
    }

    private List<AnchorPlaceGroupResponse> groupByCategory(
        List<AnchorPlace> anchorPlaces
    ) {
        Map<AnchorPlaceCategory, List<AnchorPlace>> grouped = anchorPlaces.stream()
            .collect(Collectors.groupingBy(AnchorPlace::getCategory));

        return Arrays.stream(AnchorPlaceCategory.values())
            .map(category -> new AnchorPlaceGroupResponse(
                category,
                category.getDescription(),
                grouped.getOrDefault(category, List.of()).stream()
                    .map(AnchorPlaceResponse::from)
                    .toList()
            ))
            .toList();
    }
}
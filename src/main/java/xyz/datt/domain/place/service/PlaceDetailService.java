package xyz.datt.domain.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.datt.domain.bookmark.service.PlaceBookmarkService;
import xyz.datt.domain.place.dto.PlaceDetailResponse;
import xyz.datt.domain.place.entity.PlaceMaster;
import xyz.datt.domain.place.repository.PlaceMasterRepository;
import xyz.datt.global.error.BusinessException;
import xyz.datt.global.error.ErrorCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceDetailService {
    private final PlaceMasterRepository placeMasterRepository;
    private final PlaceBookmarkService placeBookmarkService;

    public PlaceDetailResponse getPlaceDetail(Long placeId) {
        PlaceMaster placeMaster = findPlace(placeId);

        return PlaceDetailResponse.from(placeMaster, false);
    }

    public PlaceDetailResponse getPlaceDetail(Long memberId, Long placeId) {
        PlaceMaster placeMaster = findPlace(placeId);

        boolean isBookmarked = placeBookmarkService.isBookmarked(memberId, placeId);

        return PlaceDetailResponse.from(placeMaster, isBookmarked);
    }

    private PlaceMaster findPlace(Long placeId) {
        return placeMasterRepository.findById(placeId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));
    }
}
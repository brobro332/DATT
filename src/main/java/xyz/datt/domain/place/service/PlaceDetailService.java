package xyz.datt.domain.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public PlaceDetailResponse getPlaceDetail(Long placeId) {
        PlaceMaster placeMaster = placeMasterRepository.findById(placeId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

        return PlaceDetailResponse.from(placeMaster);
    }
}
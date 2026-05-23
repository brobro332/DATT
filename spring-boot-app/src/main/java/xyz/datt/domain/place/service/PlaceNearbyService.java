package xyz.datt.domain.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xyz.datt.domain.place.dto.PlaceNearbyCondition;
import xyz.datt.domain.place.dto.PlaceNearbyResponse;
import xyz.datt.domain.place.repository.PlaceMasterRepository;
import xyz.datt.global.error.BusinessException;
import xyz.datt.global.error.ErrorCode;

@Service
@RequiredArgsConstructor
public class PlaceNearbyService {
    private final PlaceMasterRepository placeMasterRepository;

    public Page<PlaceNearbyResponse> searchNearbyPlaces(
        PlaceNearbyCondition condition,
        Pageable pageable
    ) {
        validateCondition(condition);

        return placeMasterRepository.searchNearbyPlaces(condition, pageable);
    }

    private void validateCondition(PlaceNearbyCondition condition) {
        if (condition.getLon() == null || condition.getLat() == null) {
            throw new BusinessException(ErrorCode.PLACE_INVALID_SEARCH_CONDITION);
        }

        if (condition.getRadiusKm() == null || condition.getRadiusKm() <= 0) {
            throw new BusinessException(ErrorCode.PLACE_INVALID_SEARCH_CONDITION);
        }

        if (condition.getLat() < -90 || condition.getLat() > 90) {
            throw new BusinessException(ErrorCode.PLACE_INVALID_COORDINATE);
        }

        if (condition.getLon() < -180 || condition.getLon() > 180) {
            throw new BusinessException(ErrorCode.PLACE_INVALID_COORDINATE);
        }
    }
}
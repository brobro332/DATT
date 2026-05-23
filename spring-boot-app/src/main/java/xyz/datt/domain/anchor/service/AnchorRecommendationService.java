package xyz.datt.domain.anchor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.datt.domain.anchor.entity.AnchorPlaceCategory;
import xyz.datt.domain.place.dto.PlaceNearbyResponse;
import xyz.datt.domain.place.repository.PlaceMasterRepository;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnchorRecommendationService {
    private final PlaceMasterRepository placeMasterRepository;

    private static final int DEFAULT_CATEGORY_LIMIT = 5;

    public Map<AnchorPlaceCategory, List<PlaceNearbyResponse>> recommendByCategory(
        Double baseLat,
        Double baseLon,
        Double radiusKm
    ) {
        Map<AnchorPlaceCategory, List<PlaceNearbyResponse>> result =
            new EnumMap<>(AnchorPlaceCategory.class);

        for (AnchorPlaceCategory category : AnchorPlaceCategory.values()) {
            List<PlaceNearbyResponse> places =
                placeMasterRepository.findNearbyPlacesForAnchor(
                    baseLat,
                    baseLon,
                    radiusKm,
                    category.getMiddleCategoryCodes(),
                    DEFAULT_CATEGORY_LIMIT
                );

            result.put(category, places);
        }

        return result;
    }
}
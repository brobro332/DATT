package xyz.datt.domain.anchor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.datt.domain.anchor.entity.Anchor;
import xyz.datt.domain.anchor.entity.AnchorPlace;
import xyz.datt.domain.anchor.entity.AnchorPlaceCategory;
import xyz.datt.domain.anchor.repository.AnchorPlaceRepository;
import xyz.datt.domain.place.dto.PlaceNearbyResponse;
import xyz.datt.domain.place.entity.PlaceMaster;
import xyz.datt.domain.place.repository.PlaceMasterRepository;
import xyz.datt.global.error.BusinessException;
import xyz.datt.global.error.ErrorCode;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AnchorPlaceCreateService {
    private final AnchorPlaceRepository anchorPlaceRepository;
    private final PlaceMasterRepository placeMasterRepository;

    public void createAnchorPlaces(
        Anchor anchor,
        Map<AnchorPlaceCategory, List<PlaceNearbyResponse>> recommendations
    ) {
        recommendations.forEach((category, places) ->
            createCategoryPlaces(anchor, category, places)
        );
    }

    private void createCategoryPlaces(
        Anchor anchor,
        AnchorPlaceCategory category,
        List<PlaceNearbyResponse> places
    ) {
        for (int i = 0; i < places.size(); i++) {
            PlaceNearbyResponse response = places.get(i);

            PlaceMaster placeMaster = placeMasterRepository.findById(response.id())
                .orElseThrow(() -> new BusinessException(ErrorCode.PLACE_NOT_FOUND));

            AnchorPlace anchorPlace = AnchorPlace.builder()
                .anchor(anchor)
                .placeMaster(placeMaster)
                .category(category)
                .distanceKm(response.distanceKm())
                .recommendOrder(i + 1)
                .build();

            anchorPlaceRepository.save(anchorPlace);
        }
    }
}
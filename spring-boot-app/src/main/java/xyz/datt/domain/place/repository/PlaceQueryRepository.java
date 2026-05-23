package xyz.datt.domain.place.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.datt.domain.place.dto.PlaceNearbyCondition;
import xyz.datt.domain.place.dto.PlaceNearbyResponse;
import xyz.datt.domain.place.dto.PlaceSearchCondition;
import xyz.datt.domain.place.dto.PlaceSearchResponse;

import java.util.List;

public interface PlaceQueryRepository {
    Page<PlaceSearchResponse> searchPlaces(
        PlaceSearchCondition condition,
        Pageable pageable
    );

    Page<PlaceNearbyResponse> searchNearbyPlaces(
        PlaceNearbyCondition condition,
        Pageable pageable
    );

    List<PlaceNearbyResponse> findNearbyPlacesForAnchor(
        Double baseLat,
        Double baseLon,
        Double radiusKm,
        List<String> indsMclsCodes,
        int limit
    );
}
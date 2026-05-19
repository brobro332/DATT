package xyz.datt.domain.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xyz.datt.domain.place.dto.PlaceSearchCondition;
import xyz.datt.domain.place.dto.PlaceSearchResponse;
import xyz.datt.domain.place.repository.PlaceMasterRepository;

@Service
@RequiredArgsConstructor
public class PlaceSearchService {
    private final PlaceMasterRepository placeMasterRepository;

    public Page<PlaceSearchResponse> searchPlaces(
        PlaceSearchCondition condition,
        Pageable pageable
    ) {
        return placeMasterRepository.searchPlaces(condition, pageable);
    }
}
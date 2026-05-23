package xyz.datt.domain.place.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import xyz.datt.domain.place.dto.PlaceNearbyCondition;
import xyz.datt.domain.place.dto.PlaceNearbyResponse;
import xyz.datt.domain.place.service.PlaceNearbyService;
import xyz.datt.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
public class PlaceNearbyController {
    private final PlaceNearbyService placeNearbyService;

    @GetMapping("/api/places/nearby")
    public ApiResponse<Page<PlaceNearbyResponse>> searchNearbyPlaces(
        @ModelAttribute PlaceNearbyCondition condition,
        Pageable pageable
    ) {
        Page<PlaceNearbyResponse> response = placeNearbyService.searchNearbyPlaces(
            condition,
            pageable
        );

        return ApiResponse.success(response);
    }
}
package xyz.datt.domain.place.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import xyz.datt.domain.place.dto.PlaceSearchCondition;
import xyz.datt.domain.place.dto.PlaceSearchResponse;
import xyz.datt.domain.place.service.PlaceSearchService;
import xyz.datt.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
public class PlaceSearchController {
    private final PlaceSearchService placeSearchService;

    @GetMapping("/api/places")
    public ApiResponse<Page<PlaceSearchResponse>> searchPlaces(
        @ModelAttribute PlaceSearchCondition condition,
        Pageable pageable
    ) {
        Page<PlaceSearchResponse> response = placeSearchService.searchPlaces(condition, pageable);

        return ApiResponse.success(response);
    }
}
package xyz.datt.domain.place.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import xyz.datt.domain.place.dto.PlaceDetailResponse;
import xyz.datt.domain.place.service.PlaceDetailService;
import xyz.datt.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
public class PlaceDetailController {
    private final PlaceDetailService placeDetailService;

    @GetMapping("/api/places/{placeId}")
    public ApiResponse<PlaceDetailResponse> getPlaceDetail(@PathVariable Long placeId) {
        PlaceDetailResponse response = placeDetailService.getPlaceDetail(placeId);

        return ApiResponse.success(response);
    }
}
package xyz.datt.domain.place.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import xyz.datt.domain.place.dto.PlaceDetailResponse;
import xyz.datt.domain.place.service.PlaceDetailService;
import xyz.datt.global.response.ApiResponse;
import xyz.datt.global.security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
public class PlaceDetailController {
    private final PlaceDetailService placeDetailService;

    @GetMapping("/api/places/{placeId}")
    public ApiResponse<PlaceDetailResponse> getPlaceDetail(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long placeId
    ) {
        if (userDetails == null) {
            return ApiResponse.success(placeDetailService.getPlaceDetail(placeId));
        }

        return ApiResponse.success(placeDetailService.getPlaceDetail(userDetails.getMemberId(), placeId));
    }
}
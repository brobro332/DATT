package xyz.datt.domain.place.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.datt.domain.place.dto.PlaceMasterSearchResponse;
import xyz.datt.domain.place.service.PlaceMasterService;
import xyz.datt.global.response.ApiResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaceMasterController {
    private final PlaceMasterService placeMasterService;

    @GetMapping("/api/place-masters")
    public ApiResponse<List<PlaceMasterSearchResponse>> searchPlaceMasters(@RequestParam String keyword) {
        return ApiResponse.success(placeMasterService.searchByKeyword(keyword));
    }
}
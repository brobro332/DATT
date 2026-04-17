package xyz.datt.domain.place.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.datt.domain.place.dto.PlaceResponseDto;
import xyz.datt.domain.place.entity.Platform;
import xyz.datt.domain.place.service.PlaceService;
import xyz.datt.global.common.ApiResponse;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> search(
            @RequestParam(required = false) String keyword,        // 검색어
            @RequestParam(required = false) String category,       // 카테고리 (맛집, 카페 등)
            @RequestParam(required = false) Platform platform,     // 플랫폼 (NAVER, GOOGLE)
            @PageableDefault(page = 0, size = 20) Pageable pageable
    ) {
        Slice<PlaceResponseDto> results = placeService.getPlacesPage(keyword, category, platform, pageable);

        return ResponseEntity.ok()
            .body(ApiResponse.success(results));
    }
}

package xyz.datt.domain.bookmark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import xyz.datt.domain.bookmark.dto.PlaceBookmarkResponse;
import xyz.datt.domain.bookmark.service.PlaceBookmarkService;
import xyz.datt.global.response.ApiResponse;
import xyz.datt.global.security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
public class PlaceBookmarkController {
    private final PlaceBookmarkService placeBookmarkService;

    @PostMapping("/api/bookmarks/places/{placeId}")
    public ApiResponse<PlaceBookmarkResponse> addPlaceBookmark(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long placeId
    ) {
        PlaceBookmarkResponse response = placeBookmarkService.addBookmark(userDetails.getMemberId(), placeId);

        return ApiResponse.success(response);
    }

    @DeleteMapping("/api/bookmarks/places/{placeId}")
    public ApiResponse<Void> removePlaceBookmark(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long placeId
    ) {
        placeBookmarkService.removeBookmark(userDetails.getMemberId(), placeId);

        return ApiResponse.success(null);
    }

    @GetMapping("/api/bookmarks/places")
    public ApiResponse<Page<PlaceBookmarkResponse>> getMyPlaceBookmarks(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        Pageable pageable
    ) {
        Page<PlaceBookmarkResponse> response = placeBookmarkService.getMyBookmarks(
            userDetails.getMemberId(),
            pageable
        );

        return ApiResponse.success(response);
    }
}
package xyz.datt.domain.anchor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import xyz.datt.domain.anchor.dto.AnchorCreateRequest;
import xyz.datt.domain.anchor.dto.AnchorDetailResponse;
import xyz.datt.domain.anchor.dto.AnchorSummaryResponse;
import xyz.datt.domain.anchor.entity.AnchorSortType;
import xyz.datt.domain.anchor.service.AnchorCreateService;
import xyz.datt.domain.anchor.service.AnchorDetailService;
import xyz.datt.domain.anchor.service.AnchorListService;
import xyz.datt.global.response.ApiResponse;
import xyz.datt.global.security.CustomUserDetails;


@RestController
@RequiredArgsConstructor
public class AnchorController {
    private final AnchorCreateService anchorCreateService;
    private final AnchorDetailService anchorDetailService;
    private final AnchorListService anchorListService;

    @PostMapping("/api/anchors")
    public ApiResponse<AnchorDetailResponse> createAnchor(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody AnchorCreateRequest request
    ) {
        AnchorDetailResponse response = anchorCreateService.createAnchor(
            userDetails.getMemberId(),
            request
        );

        return ApiResponse.success(response);
    }

    @GetMapping("/api/anchors/{anchorId}")
    public ApiResponse<AnchorDetailResponse> getAnchorDetail(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long anchorId
    ) {
        Long memberId = userDetails == null
            ? null
            : userDetails.getMemberId();

        AnchorDetailResponse response = anchorDetailService.getAnchorDetail(
            memberId,
            anchorId
        );

        return ApiResponse.success(response);
    }

    @GetMapping("/api/anchors")
    public ApiResponse<Page<AnchorSummaryResponse>> getPublicAnchors(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(defaultValue = "LATEST") AnchorSortType sortType,
        Pageable pageable
    ) {
        Long memberId = userDetails == null
            ? null
            : userDetails.getMemberId();

        Page<AnchorSummaryResponse> response = anchorListService.getPublicAnchors(
            memberId,
            sortType,
            pageable
        );

        return ApiResponse.success(response);
    }

    @GetMapping("/api/my/anchors")
    public ApiResponse<Page<AnchorSummaryResponse>> getMyAnchors(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(defaultValue = "LATEST") AnchorSortType sortType,
        Pageable pageable
    ) {
        Page<AnchorSummaryResponse> response = anchorListService.getMyAnchors(
            userDetails.getMemberId(),
            sortType,
            pageable
        );

        return ApiResponse.success(response);
    }

    @GetMapping("/api/anchors/popular")
    public ApiResponse<Page<AnchorSummaryResponse>> getPopularAnchors(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        Pageable pageable
    ) {
        Long memberId = userDetails == null
            ? null
            : userDetails.getMemberId();

        Page<AnchorSummaryResponse> response = anchorListService.getPopularAnchors(
            memberId,
            pageable
        );

        return ApiResponse.success(response);
    }
}
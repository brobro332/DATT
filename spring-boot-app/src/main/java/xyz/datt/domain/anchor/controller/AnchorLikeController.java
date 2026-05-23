package xyz.datt.domain.anchor.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import xyz.datt.domain.anchor.service.AnchorLikeService;
import xyz.datt.global.response.ApiResponse;
import xyz.datt.global.security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
public class AnchorLikeController {
    private final AnchorLikeService anchorLikeService;

    @PostMapping("/api/anchors/{anchorId}/likes")
    public ApiResponse<Void> likeAnchor(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long anchorId
    ) {
        anchorLikeService.likeAnchor(userDetails.getMemberId(), anchorId);

        return ApiResponse.success(null);
    }

    @DeleteMapping("/api/anchors/{anchorId}/likes")
    public ApiResponse<Void> unlikeAnchor(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long anchorId
    ) {
        anchorLikeService.unlikeAnchor(userDetails.getMemberId(), anchorId);

        return ApiResponse.success(null);
    }
}
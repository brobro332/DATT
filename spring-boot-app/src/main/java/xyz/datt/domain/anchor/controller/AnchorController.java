package xyz.datt.domain.anchor.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.datt.domain.anchor.dto.AnchorResponseDto;
import xyz.datt.domain.anchor.service.AnchorService;
import xyz.datt.global.common.ApiResponse;

@RestController
@RequestMapping("/api/v1/anchors")
@RequiredArgsConstructor
public class AnchorController {
    private final AnchorService anchorService;

    @PostMapping
    public ResponseEntity<ApiResponse<AnchorResponseDto>> createAnchor(@RequestParam("keyword") String keyword) throws Exception {
        return ResponseEntity.ok(ApiResponse.success(anchorService.createAnchor(keyword)));
    }

    @GetMapping("/{anchorId}")
    public ResponseEntity<ApiResponse<AnchorResponseDto>> getAnchor(@PathVariable String anchorId) {
        return ResponseEntity.ok(ApiResponse.success(anchorService.getAnchor(anchorId)));
    }
}

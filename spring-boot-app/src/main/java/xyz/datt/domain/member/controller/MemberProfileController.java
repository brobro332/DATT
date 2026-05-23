package xyz.datt.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.datt.domain.member.dto.MemberProfileResponse;
import xyz.datt.domain.member.service.MemberProfileService;
import xyz.datt.global.response.ApiResponse;
import xyz.datt.global.security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
public class MemberProfileController {
    private final MemberProfileService memberProfileService;

    @GetMapping("/api/my/profile")
    public ApiResponse<MemberProfileResponse> getMyProfile(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MemberProfileResponse response = memberProfileService.getMyProfile(
            userDetails.getMemberId()
        );

        return ApiResponse.success(response);
    }
}
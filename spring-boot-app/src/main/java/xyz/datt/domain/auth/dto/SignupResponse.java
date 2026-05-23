package xyz.datt.domain.auth.dto;

public record SignupResponse(
        Long memberId,
        String email,
        String nickname
) {
}
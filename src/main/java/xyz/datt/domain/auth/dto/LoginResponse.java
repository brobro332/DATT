package xyz.datt.domain.auth.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        Long memberId,
        String nickname
) {
}
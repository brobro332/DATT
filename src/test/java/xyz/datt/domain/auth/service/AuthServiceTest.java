package xyz.datt.domain.auth.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import xyz.datt.domain.auth.dto.LoginRequest;
import xyz.datt.domain.auth.dto.LoginResponse;
import xyz.datt.domain.auth.dto.LogoutRequest;
import xyz.datt.domain.auth.dto.SignupRequest;
import xyz.datt.domain.auth.dto.TokenReissueRequest;
import xyz.datt.domain.auth.dto.TokenReissueResponse;
import xyz.datt.domain.auth.entity.RefreshToken;
import xyz.datt.domain.auth.repository.RefreshTokenRepository;
import xyz.datt.domain.member.entity.Member;
import xyz.datt.domain.member.repository.MemberRepository;
import xyz.datt.global.error.BusinessException;
import xyz.datt.global.error.ErrorCode;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("로그인 성공 시 Refresh Token이 저장된다.")
    void givenValidCredential_whenLogin_thenSaveRefreshToken() {
        authService.signup(new SignupRequest(
            "test@datt.xyz",
            "password123",
            "테스트유저"
        ));

        LoginResponse response = authService.login(new LoginRequest(
            "test@datt.xyz",
            "password123"
        ));

        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.refreshToken()).isNotBlank();
        assertThat(refreshTokenRepository.findByMemberId(response.memberId()))
            .isPresent();
    }

    @Test
    @DisplayName("Refresh Token으로 Access Token 재발급에 성공한다.")
    void givenValidRefreshToken_whenReissue_thenCreateAccessToken() {
        authService.signup(new SignupRequest(
            "test@datt.xyz",
            "password123",
            "테스트유저"
        ));

        LoginResponse loginResponse = authService.login(new LoginRequest(
            "test@datt.xyz",
            "password123"
        ));

        TokenReissueResponse response = authService.reissue(
            new TokenReissueRequest(loginResponse.refreshToken())
        );

        assertThat(response.accessToken()).isNotBlank();
    }

    @Test
    @DisplayName("저장되지 않은 Refresh Token이면 재발급에 실패한다.")
    void givenInvalidRefreshToken_whenReissue_thenThrowException() {
        assertThatThrownBy(() -> authService.reissue(
            new TokenReissueRequest("invalid-refresh-token")
        ))
            .isInstanceOf(BusinessException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.INVALID_REFRESH_TOKEN);
    }

    @Test
    @DisplayName("만료된 Refresh Token이면 재발급에 실패한다.")
    void givenExpiredRefreshToken_whenReissue_thenThrowException() {
        Member member = memberRepository.save(Member.createUser(
            "test@datt.xyz",
            passwordEncoder.encode("password123"),
            "테스트유저"
        ));

        String refreshToken = "expired-refresh-token";

        refreshTokenRepository.save(RefreshToken.create(
            member.getId(),
            refreshToken,
            LocalDateTime.now().minusDays(1)
        ));

        assertThatThrownBy(() -> authService.reissue(
                new TokenReissueRequest(refreshToken)
        ))
            .isInstanceOf(BusinessException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.EXPIRED_REFRESH_TOKEN);
    }

    @Test
    @DisplayName("로그아웃 시 Refresh Token이 삭제된다.")
    void givenValidRefreshToken_whenLogout_thenDeleteRefreshToken() {
        authService.signup(new SignupRequest(
            "test@datt.xyz",
            "password123",
            "테스트유저"
        ));

        LoginResponse loginResponse = authService.login(new LoginRequest(
            "test@datt.xyz",
            "password123"
        ));

        authService.logout(new LogoutRequest(loginResponse.refreshToken()));

        assertThat(refreshTokenRepository.findByToken(loginResponse.refreshToken()))
            .isEmpty();
    }

    @Test
    @DisplayName("저장되지 않은 Refresh Token이면 로그아웃에 실패한다.")
    void givenInvalidRefreshToken_whenLogout_thenThrowException() {
        assertThatThrownBy(() -> authService.logout(
            new LogoutRequest("invalid-refresh-token")
        ))
            .isInstanceOf(BusinessException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.INVALID_REFRESH_TOKEN);
    }
}
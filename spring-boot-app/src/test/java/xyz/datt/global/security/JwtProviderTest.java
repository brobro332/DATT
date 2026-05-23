package xyz.datt.global.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class JwtProviderTest {
    @Autowired
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("Access Token 생성 및 파싱에 성공한다.")
    void givenMemberInfo_whenCreateToken_thenParseSuccess() {
        String token = jwtProvider.createAccessToken(
            1L,
            "USER"
        );

        Long memberId = jwtProvider.getMemberId(token);
        String role = jwtProvider.getRole(token);

        assertThat(memberId).isEqualTo(1L);
        assertThat(role).isEqualTo("USER");
    }
}
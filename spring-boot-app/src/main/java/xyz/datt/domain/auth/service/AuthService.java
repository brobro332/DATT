package xyz.datt.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.datt.domain.auth.dto.*;
import xyz.datt.domain.auth.entity.RefreshToken;
import xyz.datt.domain.auth.repository.RefreshTokenRepository;
import xyz.datt.domain.member.entity.Member;
import xyz.datt.domain.member.repository.MemberRepository;
import xyz.datt.global.error.BusinessException;
import xyz.datt.global.error.ErrorCode;
import xyz.datt.global.security.JwtProvider;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        validateEmail(request.email());
        validateNickname(request.nickname());

        String encodedPassword = passwordEncoder.encode(request.password());

        Member member = Member.createUser(
            request.email(),
            encodedPassword,
            request.nickname()
        );

        Member savedMember = memberRepository.save(member);

        return new SignupResponse(
            savedMember.getId(),
            savedMember.getEmail(),
            savedMember.getNickname()
        );
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
            .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtProvider.createAccessToken(
            member.getId(),
            member.getRole().name()
        );

        String refreshToken = jwtProvider.createRefreshToken(
                member.getId()
        );

        saveOrUpdateRefreshToken(
            member.getId(),
            refreshToken,
            jwtProvider.getRefreshTokenExpiredAt()
        );

        return new LoginResponse(
            accessToken,
            refreshToken,
            member.getId(),
            member.getNickname()
        );
    }

    @Transactional(readOnly = true)
    public TokenReissueResponse reissue(TokenReissueRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
            .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN));

        if (refreshToken.isExpired()) {
            throw new BusinessException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        jwtProvider.validateToken(request.refreshToken());

        Member member = memberRepository.findById(refreshToken.getMemberId())
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        String accessToken = jwtProvider.createAccessToken(
            member.getId(),
            member.getRole().name()
        );

        return new TokenReissueResponse(accessToken);
    }

    @Transactional
    public void logout(LogoutRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
            .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN));

        refreshTokenRepository.delete(refreshToken);
    }

    private void saveOrUpdateRefreshToken(
        Long memberId,
        String token,
        LocalDateTime expiredAt
    ) {
        refreshTokenRepository.findByMemberId(memberId)
            .ifPresentOrElse(
                refreshToken -> refreshToken.updateToken(token, expiredAt),
                () -> refreshTokenRepository.save(
                    RefreshToken.create(memberId, token, expiredAt)
                )
            );
    }

    private void validateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.DUPLICATED_EMAIL);
        }
    }

    private void validateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new BusinessException(ErrorCode.DUPLICATED_NICKNAME);
        }
    }
}

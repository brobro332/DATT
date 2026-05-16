package xyz.datt.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "common.internal_server_error", "서버 내부 오류가 발생했습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "common.invalid_input_value", "요청 값이 올바르지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "common.method_not_allowed", "지원하지 않는 HTTP 메서드입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "auth.unauthorized", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "auth.forbidden", "접근 권한이 없습니다."),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "member.not_found", "사용자를 찾을 수 없습니다."),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "member.duplicated_email", "이미 사용 중인 이메일입니다."),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "member.duplicated_nickname", "이미 사용 중인 닉네임입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "member.invalid_password", "비밀번호가 올바르지 않습니다."),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "auth.invalid_token", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "auth.expired_token", "만료된 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "auth.refresh_token_not_found", "Refresh Token을 찾을 수 없습니다."),

    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "place.not_found", "장소를 찾을 수 없습니다."),
    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "place.invalid_category", "유효하지 않은 카테고리입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public int getStatus() {
        return httpStatus.value();
    }
}
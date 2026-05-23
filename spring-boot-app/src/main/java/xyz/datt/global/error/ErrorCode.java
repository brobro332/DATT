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
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "auth.invalid_refresh_token", "유효하지 않은 Refresh Token입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "auth.expired_refresh_token", "만료된 Refresh Token입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "auth.refresh_token_not_found", "Refresh Token을 찾을 수 없습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "auth.invalid_credentials", "이메일 또는 비밀번호가 올바르지 않습니다."),

    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "place.not_found", "장소를 찾을 수 없습니다."),
    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "place.invalid_category", "유효하지 않은 카테고리입니다."),
    PLACE_INVALID_SEARCH_CONDITION(HttpStatus.BAD_REQUEST, "place.invalid_search_condition", "장소 검색 조건이 올바르지 않습니다."),
    PLACE_INVALID_COORDINATE(HttpStatus.BAD_REQUEST, "place.invalid_coordinate", "좌표 값이 올바르지 않습니다."),
    PLACE_BOOKMARK_ALREADY_EXISTS(HttpStatus.CONFLICT, "place_bookmark.already_exists", "이미 저장한 장소입니다."),
    PLACE_BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "place_bookmark.not_found", "저장한 장소를 찾을 수 없습니다."),

    PLACE_REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "place_review.not_found", "리뷰를 찾을 수 없습니다."),
    PLACE_REVIEW_ACCESS_DENIED(HttpStatus.FORBIDDEN, "place_review.access_denied", "리뷰에 접근할 수 없습니다."),
    PLACE_REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "place_review.already_exists", "이미 리뷰를 작성한 장소입니다."),
    INVALID_REVIEW_RATING(HttpStatus.BAD_REQUEST, "review.invalid_rating", "평점은 1점 이상 5점 이하만 가능합니다."),

    ANCHOR_LIKE_ALREADY_EXISTS(HttpStatus.CONFLICT, "anchor_like.already_exists", "이미 좋아요를 누른 Anchor입니다."),
    ANCHOR_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "anchor_like.not_found", "Anchor 좋아요를 찾을 수 없습니다."),
    ANCHOR_NOT_FOUND(HttpStatus.NOT_FOUND, "anchor.not_found", "Anchor를 찾을 수 없습니다."),
    ANCHOR_ACCESS_DENIED(HttpStatus.FORBIDDEN, "anchor.access_denied", "Anchor에 접근할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public int getStatus() {
        return httpStatus.value();
    }
}
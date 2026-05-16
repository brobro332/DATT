package xyz.datt.global.response;

import xyz.datt.global.error.ErrorCode;

public record ErrorResponse(
    String code,
    String message,
    int status
) {
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(
            errorCode.getCode(),
            errorCode.getMessage(),
            errorCode.getStatus()
        );
    }
}

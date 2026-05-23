package xyz.datt.global.response;

import xyz.datt.global.error.ErrorCode;

import java.util.List;

public record ValidationErrorResponse(
    String code,
    String message,
    int status,
    List<FieldErrorResponse> errors
) {

    public static ValidationErrorResponse of(
            ErrorCode errorCode,
            List<FieldErrorResponse> errors
    ) {
        return new ValidationErrorResponse(
            errorCode.getCode(),
            errorCode.getMessage(),
            errorCode.getStatus(),
            errors
        );
    }
}
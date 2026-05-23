package xyz.datt.global.response;

public record FieldErrorResponse(
    String field,
    String message
) {
}

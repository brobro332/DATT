package xyz.datt.global.error;

import org.slf4j.MDC;
import xyz.datt.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.datt.global.response.ErrorResponse;
import xyz.datt.global.response.FieldErrorResponse;
import xyz.datt.global.response.ValidationErrorResponse;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException exception,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = exception.getErrorCode();

        log.warn(
            "event=business_exception traceId={} path={} code={} message={}",
            MDC.get("traceId"),
            request.getRequestURI(),
            errorCode.getCode(),
            exception.getMessage()
        );

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ApiResponse.fail(
                ErrorResponse.from(errorCode)
            ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ValidationErrorResponse>> handleValidationException(
        MethodArgumentNotValidException exception,
        HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

        List<FieldErrorResponse> fieldErrors = exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> new FieldErrorResponse(
                error.getField(),
                error.getDefaultMessage()
            ))
            .toList();

        ValidationErrorResponse errorResponse = ValidationErrorResponse.of(
            errorCode,
            fieldErrors
        );

        log.warn(
            "event=validation_failed traceId={} path={} errors={}",
            MDC.get("traceId"),
            request.getRequestURI(),
            fieldErrors
        );

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(new ApiResponse<>(
                false,
                errorResponse,
                null
            ));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotAllowedException(
        HttpRequestMethodNotSupportedException exception,
        HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;

        log.warn(
            "event=method_not_allowed traceId={} path={} method={}",
            MDC.get("traceId"),
            request.getRequestURI(),
            request.getMethod()
        );

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ApiResponse.fail(
                ErrorResponse.from(errorCode)
            ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(
        Exception exception,
        HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        log.error(
            "event=unexpected_exception traceId={} path={} message={}",
            MDC.get("traceId"),
            request.getRequestURI(),
            exception.getMessage(),
            exception
        );

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ApiResponse.fail(
                ErrorResponse.from(errorCode)
            ));
    }
}
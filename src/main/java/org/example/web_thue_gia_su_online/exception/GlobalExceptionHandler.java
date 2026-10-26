package org.example.web_thue_gia_su_online.exception;

import java.util.Map;
import java.util.Objects;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;

import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    // Exception handling
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<Void>> handlingRunTimeException(RuntimeException exception) {
        log.error("Unhandled exception: {}", exception.getMessage(), exception);
        ApiResponse<Void> apiResponse = new ApiResponse<>();

        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse<Void>> handlingAccessDeniedException(AccessDeniedException error) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();

        apiResponse.setCode(ErrorCode.UNAUTHORIZED.getCode());
        apiResponse.setMessage(ErrorCode.UNAUTHORIZED.getMessage());

        return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AssertionError.class)
    ResponseEntity<ApiResponse<Void>> handlingAssertionError(AssertionError error) {
        ApiResponse<Void> apiResponse = new ApiResponse<Void>();

        apiResponse.setCode(ErrorCode.USER_NOT_LOG_IN.getCode());
        apiResponse.setMessage(ErrorCode.USER_NOT_LOG_IN.getMessage());

        return ResponseEntity.status(ErrorCode.USER_NOT_LOG_IN.getStatusCode()).body(apiResponse);
    }

    // Exception handling bang cac class exception tu cau hinh
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<Void>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse<Void> apiResponse = new ApiResponse<>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    // Bắt EntityNotFoundException (dùng trong ConversationService)
    @ExceptionHandler(value = EntityNotFoundException.class)
    ResponseEntity<ApiResponse<Void>> handlingEntityNotFoundException(EntityNotFoundException exception) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.USER_NOT_FOUND.getCode());
        apiResponse.setMessage(exception.getMessage());
        return ResponseEntity.status(ErrorCode.USER_NOT_FOUND.getStatusCode()).body(apiResponse);
    }

    // Bắt IllegalArgumentException
    @ExceptionHandler(value = IllegalArgumentException.class)
    ResponseEntity<ApiResponse<Void>> handlingIllegalArgumentException(IllegalArgumentException exception) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.INVALID_KEY.getCode());
        apiResponse.setMessage(exception.getMessage());
        return ResponseEntity.status(ErrorCode.INVALID_KEY.getStatusCode()).body(apiResponse);
    }

    // Bắt IllegalStateException
    @ExceptionHandler(value = IllegalStateException.class)
    ResponseEntity<ApiResponse<Void>> handlingIllegalStateException(IllegalStateException exception) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.INVALID_KEY.getCode());
        apiResponse.setMessage(exception.getMessage());
        return ResponseEntity.status(ErrorCode.INVALID_KEY.getStatusCode()).body(apiResponse);
    }

    // Invalid Handling
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Void>> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;
        try {
            errorCode = ErrorCode.valueOf(enumKey);

            var constraintViolation =
                    exception.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

            attributes = constraintViolation.getConstraintDescriptor().getAttributes();

            log.info(attributes.toString());

        } catch (IllegalArgumentException e) {

        }
        ApiResponse<Void> apiResponse = new ApiResponse<>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(
                Objects.nonNull(attributes)
                        ? mapAttribute(errorCode.getMessage(), attributes)
                        : errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}

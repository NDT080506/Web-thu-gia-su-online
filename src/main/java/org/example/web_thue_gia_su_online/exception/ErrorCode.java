package org.example.web_thue_gia_su_online.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    USER_EXISTED(1001, "User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND),
    UNCATEGORIZED_EXCEPTION(999, "uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1005, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    USER_NOT_LOG_IN(1006, "User not log in", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1009, "Uncategorized error", HttpStatus.BAD_REQUEST),
    TUTOR_APPLICATION_NOT_FOUND(1010, "Tutor application not found", HttpStatus.NOT_FOUND),
    TUTOR_APPLICATION_EXISTED(1011, "Tutor application existed", HttpStatus.BAD_REQUEST),
    TUTOR_PROFILE_EXISTED(1012, "Tutor profile existed", HttpStatus.BAD_REQUEST),
    TUTOR_PROFILE_NOT_FOUND(1013, "Tutor profile not found", HttpStatus.NOT_FOUND),
    EMAIL_NOT_VERIFIED(1014, "Email not verified", HttpStatus.UNAUTHORIZED),
    EMAIL_EXISTED(1015, "Email existed", HttpStatus.BAD_REQUEST),
    ARTICLE_NOT_FOUND(1016, "Article not found", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND(1017, "Comment not found", HttpStatus.NOT_FOUND);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    int code;
    String message;
    HttpStatusCode statusCode;
}

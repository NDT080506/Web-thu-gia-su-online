package org.example.web_thue_gia_su_online.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(999, "uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND),
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
    BOOKING_NOT_FOUND(1016, "Booking not found", HttpStatus.NOT_FOUND),
    COURSE_ENROLLMENT_NOT_FOUND(1017, "Course enrollment not found", HttpStatus.NOT_FOUND),
    REVIEW_NOT_FOUND(1018, "Review not found", HttpStatus.NOT_FOUND),
    MESSAGE_NOT_FOUND(1019, "Message not found", HttpStatus.NOT_FOUND),
    CONVERSATION_NOT_FOUND(1020, "Conversation not found", HttpStatus.NOT_FOUND),
    COURSE_NOT_FOUND(1021, "Course not found", HttpStatus.NOT_FOUND),
    LESSON_NOT_FOUND(1022, "Lesson not found", HttpStatus.NOT_FOUND),
    ARTICLE_NOT_FOUND(1023, "Article not found", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND(1024, "Comment not found", HttpStatus.NOT_FOUND),
    PACKAGE_NOT_FOUND(1025, "Package not found", HttpStatus.NOT_FOUND),
    BOOKING_ALREADY_COMPLETED(1026, "Booking already completed", HttpStatus.BAD_REQUEST),
    BOOKING_CANCELLED(1027, "Booking has been cancelled", HttpStatus.BAD_REQUEST),
    BOOKING_NOT_PAID(1028, "Booking not paid", HttpStatus.BAD_REQUEST),
    COURSE_ENROLLMENT_ALREADY_COMPLETED(1029, "Course enrollment already completed", HttpStatus.BAD_REQUEST),
    COURSE_ENROLLMENT_NOT_ACTIVE(1030, "Course enrollment not active", HttpStatus.BAD_REQUEST),
    WALLET_NOT_FOUND(1031, "Wallet not found", HttpStatus.NOT_FOUND),
    TRANSACTION_NOT_FOUND(1032, "Transaction not found", HttpStatus.NOT_FOUND),
    BOOKING_HAS_ESCROW_HOLD(1033, "Cannot cancel booking with escrow hold", HttpStatus.BAD_REQUEST),
    ENROLLMENT_HAS_ESCROW_HOLD(1034, "Cannot cancel enrollment with escrow hold", HttpStatus.BAD_REQUEST),
    WITHDRAWAL_NOT_FOUND(1035, "Withdrawal not found", HttpStatus.NOT_FOUND),
    INSUFFICIENT_BALANCE(1036, "Insufficient balance", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    int code;
    String message;
    HttpStatusCode statusCode;
}

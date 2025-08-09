package com.trustai.nft_app.exception;


import com.trustai.common.dto.ErrorField;
import com.trustai.common.dto.ErrorResponse;
import com.trustai.common.exception.ErrorCode;
import com.trustai.common.exception.RestCallException;
import com.trustai.common.exception.ValidationException;
import com.trustai.user_service.user.exception.AuthenticationException;
import com.trustai.user_service.user.exception.UserCreateException;
import com.trustai.user_service.user.exception.base.UserValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;


/*
Summary of Log Level Choices for Your Exceptions
| Exception / Handler                                                | Log Level | Reasoning / Explanation                                                                                                        |
| ------------------------------------------------------------------ | --------- | ------------------------------------------------------------------------------------------------------------------------------ |
| **handleMethodArgumentNotValid (MethodArgumentNotValidException)** | `warn`    | Input validation error from client; expected and common, so warn to highlight but not error.                                   |
| **handleHttpMessageNotReadable (HttpMessageNotReadableException)** | `warn`    | Client sent malformed or missing body; expected client error, not server failure.                                              |
| **handleDataIntegrityViolation (DataIntegrityViolationException)** | `warn`    | Database constraint violation usually caused by client data issues (duplicates, nulls); important but not system failure.      |
| **handleAllExceptions (UserValidationException)**                  | `warn`    | User input validation failure; expected client error, so warn level is appropriate.                                            |
| **handleUserCreateException (UserCreateException)**                | `error`   | User creation failures may indicate internal bugs or logic errors; should be elevated to error to get attention and alerts.    |
| **handleIllegalArgumentException (IllegalArgumentException)**      | `warn`    | Usually due to invalid arguments passed by client; warn level appropriate for client-induced errors.                           |
| **handleAuthenticationException (AuthenticationException)**        | `warn`    | Authentication failures expected as part of normal flow (bad credentials); warn level to alert but not error.                  |
| **handleValidationException (ValidationException)**                | `warn`    | Business rule validation failure, expected but important; warn level fits.                                                     |
| **handleRuntimeException (RuntimeException)**                      | `error`   | Unexpected runtime exceptions often indicate server issues; log as error to alert ops and developers.                          |
| **handleGenericException (Exception)**                             | `error`   | Catch-all for any unhandled exceptions; logs unexpected server-side failures as error to ensure visibility and quick response. |

 */
@RestControllerAdvice
@Slf4j
public class CommonGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Value("${spring.profiles.active:local}")
    private String activeProfile;


    // Handle validation errors for @Valid annotated parameters
    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        maybePrintStackTrace(ex);
        String traceId = MDC.get("traceId");
        String userId = "1"; // Replace with actual userId retrieval logic
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        log.warn(ex.getMessage());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        List<ErrorField> errorFields = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorField(error.getField(), error.getDefaultMessage()))
                .toList();

        // This is caused by invalid method arguments from client input (e.g., failed @Valid annotations).

        log.warn("MethodArgumentNotValidException: traceId={}, userId={}, path={}, errors={}",
                traceId, userId, path, errors, ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                String.join("; ", errors),
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );
        errorResponse.setErrorFields(errorFields);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Handle malformed or missing request body
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        maybePrintStackTrace(ex);
        String traceId = MDC.get("traceId");
        String userId = "1"; // Replace with actual userId retrieval logic
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        log.warn("HttpMessageNotReadableException: traceId={}, userId={}, path={}, message={}",
                traceId, userId, path, ex.getMessage(), ex);


        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Request body is missing or malformed",
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        maybePrintStackTrace(ex);

        String traceId = MDC.get("traceId");
        String userId = "1"; // Replace with actual userId retrieval logic
        String path = request.getRequestURI();

        Throwable rootCause = ex.getRootCause();
        String message = "Data integrity violation";

        if (rootCause != null && rootCause.getMessage() != null) {
            String causeMessage = rootCause.getMessage();

            if (causeMessage.contains("Duplicate entry")) {
                if (causeMessage.contains("username")) {
                    message = "Username already exists.";
                } else if (causeMessage.contains("referral_code")) {
                    message = "Referral code already exists.";
                } else {
                    message = "Duplicate entry exists.";
                }
            } else if (causeMessage.contains("cannot be null")) {
                message = "Required field is missing.";
            }
        }

        log.warn("DataIntegrityViolationException: traceId={}, userId={}, path={}, rootCauseMessage={}, resolvedMessage={}",
                traceId, userId, path, rootCause != null ? rootCause.getMessage() : "N/A", message, ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Database Constraint Violation",
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(UserValidationException ex, HttpServletRequest request) {
        maybePrintStackTrace(ex);
        String traceId = MDC.get("traceId");
        String userId = "1"; // Replace with actual userId retrieval logic
        String path = request.getRequestURI();

        log.warn("UserValidationException: traceId={}, userId={}, path={}, message={}",
                traceId, userId, path, ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "UserValidationException",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UserCreateException.class)
    public ResponseEntity<ErrorResponse> handleUserCreateException(UserCreateException ex, HttpServletRequest request) {
        maybePrintStackTrace(ex);
        String traceId = MDC.get("traceId");
        String userId = "1"; // Replace with actual userId retrieval logic
        String path = request.getRequestURI();

        log.error("UserCreateException: traceId={}, userId={}, path={}, message={}",
                traceId, userId, path, ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Exception",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        maybePrintStackTrace(ex);
        String traceId = MDC.get("traceId");
        String userId = "1"; // Replace with actual userId retrieval logic
        String path = request.getRequestURI();

        log.warn("IllegalArgumentException: traceId={}, userId={}, path={}, message={}",
                traceId, userId, path, ex.getMessage(), ex);


        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "IllegalArgument Exception",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        maybePrintStackTrace(ex);
        String traceId = MDC.get("traceId");
        String userId = "1"; // Replace with actual userId retrieval logic
        String path = request.getRequestURI();

        log.warn("AuthenticationException: traceId={}, userId={}, path={}, message={}",
                traceId, userId, path, ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Authentication Exception",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex, WebRequest request) {
        maybePrintStackTrace(ex);
        String traceId = MDC.get("traceId");
        String userId = "1"; // Replace with actual userId retrieval logic
        String path = request.getDescription(false);

        log.warn("ValidationException occurred: traceId={}, userId={}, errorCode={}, message={}, path={}",
                traceId, userId, ex.getErrorCode(), ex.getMessage(), path, ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "ValidationException",
                ex.getErrorCode(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RestCallException.class)
    public ResponseEntity<ErrorResponse> handleRestCallException(RestCallException ex, WebRequest request) {
        maybePrintStackTrace(ex);
        String traceId = MDC.get("traceId");
        String userId = "1"; // Replace with actual userId retrieval logic
        String path = request.getDescription(false);

        log.warn("RestCallException: traceId={}, userId={}, message={}, path={}",
                traceId, userId, ex.getMessage(), path, ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "RestCallException",
                ErrorCode.REST_CALL_EXCEPTION,
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // Catch all RuntimeExceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
        maybePrintStackTrace(ex);
        String traceId = MDC.get("traceId");
        String userId = "1"; // Replace with actual userId retrieval logic
        String path = request.getDescription(false);

        log.error("RuntimeException occurred: traceId={}, userId={}, path={}, message={}",
                traceId, userId, path, ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Runtime Exception",
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle other unexpected exceptions
    /*@ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }*/

    // Optional: Handle generic Exception as a fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        maybePrintStackTrace(ex);
        String traceId = MDC.get("traceId");
        String userId = "1"; // Replace with actual userId retrieval logic
        String path = request.getDescription(false);

        log.error("Unhandled Exception occurred: traceId={}, userId={}, path={}, message={}",
                traceId, userId, path, ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Exception",
                ex.getMessage(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void maybePrintStackTrace(Exception ex) {
        if ("local".equalsIgnoreCase(activeProfile)) {
            ex.printStackTrace();
        }
    }
}

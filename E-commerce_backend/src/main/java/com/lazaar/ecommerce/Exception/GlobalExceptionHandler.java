package com.lazaar.ecommerce.Exception;

import com.lazaar.ecommerce.DTO.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Turns every exception thrown by a controller into a JSON ApiResponse {status, message},
 * so the frontend can always read err.error.message.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // Wrong email/password (BadCredentialsException), disabled or locked account...
  // Same message in every case so the API does not reveal which emails exist.
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException ex) {
    return error(HttpStatus.UNAUTHORIZED, "Invalid email or password");
  }

  @ExceptionHandler(UserNotVerifiedException.class)
  public ResponseEntity<ApiResponse> handleUserNotVerifiedException(UserNotVerifiedException ex) {
    return error(HttpStatus.FORBIDDEN, ex.getMessage());
  }

  @ExceptionHandler(EmailAlreadyUsedException.class)
  public ResponseEntity<ApiResponse> handleEmailAlreadyUsedException(EmailAlreadyUsedException ex) {
    return error(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(OTPExpiredException.class)
  public ResponseEntity<ApiResponse> handleOTPExpiredException(OTPExpiredException ex) {
    return error(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(TokenRefreshException.class)
  public ResponseEntity<ApiResponse> handleTokenRefreshException(TokenRefreshException ex) {
    return error(HttpStatus.FORBIDDEN, "Refresh token is invalid or expired. Please log in again");
  }

  // Thrown by @PreAuthorize / @Secured: without this handler the generic one below would turn it into a 500
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
    return error(HttpStatus.FORBIDDEN, "Access denied");
  }

  // Missing or malformed JSON body
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    return error(HttpStatus.BAD_REQUEST, "Malformed request body");
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
    String message = "Data conflict";

    Throwable rootCause = ex.getRootCause();
    if (rootCause instanceof SQLIntegrityConstraintViolationException) {
      String rootMessage = rootCause.getMessage();
      if (rootMessage.contains("username")) {
        message = "Username is already taken";
      } else if (rootMessage.contains("email")) {
        message = "Email is already registered";
      }
    }

    return error(HttpStatus.CONFLICT, message);
  }

  // Fallback for everything else
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse> handleException(Exception ex) {
    // Spring MVC exceptions (malformed JSON, missing parameter, unknown route...) already carry the right status
    if (ex instanceof ErrorResponse errorResponse) {
      HttpStatusCode status = errorResponse.getStatusCode();
      return ResponseEntity.status(status).body(new ApiResponse("error", errorResponse.getBody().getDetail()));
    }

    // Unexpected bug: full stack trace in the logs, generic message for the client
    log.error("Unexpected error", ex);
    return error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
  }

  private ResponseEntity<ApiResponse> error(HttpStatus status, String message) {
    return ResponseEntity.status(status).body(new ApiResponse("error", message));
  }
}

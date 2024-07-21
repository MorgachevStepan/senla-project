package com.stepanew.senlaproject.controller.handler;

import com.stepanew.senlaproject.exceptions.AuthException;
import com.stepanew.senlaproject.exceptions.UserException;
import com.stepanew.senlaproject.exceptions.message.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> handleAuthException(AccessDeniedException ex) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String message = ex.getMessage() + " to user with email: %s"
                .formatted(email);

        return ResponseEntity
                .status(FORBIDDEN)
                .body(new ErrorMessage(FORBIDDEN.name(), message));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorMessage> handleAuthException(AuthException e) {
        AuthException.CODE code = e.getCode();
        HttpStatus status = switch (code) {
            case NO_SUCH_EMAIL_OR_PASSWORD -> HttpStatus.NOT_FOUND;
            case JWT_VALIDATION_ERROR -> HttpStatus.UNAUTHORIZED;
            case INVALID_REPEAT_PASSWORD -> HttpStatus.BAD_REQUEST;
            case EMAIL_IN_USE -> HttpStatus.CONFLICT;
        };
        String codeStr = code.toString();
        return ResponseEntity
                .status(status)
                .body(new ErrorMessage(codeStr, e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorMessage> handleAuthException(AuthenticationException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(HttpStatus.BAD_REQUEST.name(), e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<ErrorMessage> handleAuthException(HttpClientErrorException.Unauthorized e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorMessage(HttpStatus.UNAUTHORIZED.name(), e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorMessage> handleUserException(UserException e) {
        UserException.CODE code = e.getCode();
        HttpStatus status = switch (code) {
            case NO_SUCH_USER -> HttpStatus.NOT_FOUND;
        };
        String codeStr = code.toString();
        return ResponseEntity
                .status(status)
                .body(new ErrorMessage(codeStr, e.getMessage()));
    }

}

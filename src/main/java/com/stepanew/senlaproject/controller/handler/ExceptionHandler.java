package com.stepanew.senlaproject.controller.handler;

import com.stepanew.senlaproject.exceptions.*;
import com.stepanew.senlaproject.exceptions.message.ErrorMessage;
import com.stepanew.senlaproject.exceptions.message.ValidationErrorMessage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;
import java.util.stream.Collectors;

import static com.stepanew.senlaproject.utils.StringUtil.trimToFirstDot;
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

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorMessage> handleValidationException(MethodArgumentNotValidException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String codeStr = "INCORRECT_INPUT";

        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));

        ValidationErrorMessage errorMessage = new ValidationErrorMessage(
                codeStr,
                "Validation failed",
                errors
        );

        return ResponseEntity
                .status(status)
                .body(errorMessage);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorMessage> handleConstraintViolationException(ConstraintViolationException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String codeStr = "INCORRECT_INPUT";

        Map<String, String> errors = e.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        violation -> trimToFirstDot(violation.getPropertyPath().toString()),
                        ConstraintViolation::getMessage
                ));

        ValidationErrorMessage errorMessage = new ValidationErrorMessage(
                codeStr,
                "Validation failed",
                errors
        );

        return ResponseEntity
                .status(status)
                .body(errorMessage);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorMessage> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String codeStr = "MISSING_PARAMETER";

        String message = String.format("Parameter '%s' is missing", e.getParameterName());

        ErrorMessage errorMessage = new ErrorMessage(
                codeStr,
                message
        );

        return ResponseEntity.status(status).body(errorMessage);
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

    @org.springframework.web.bind.annotation.ExceptionHandler(StoreException.class)
    public ResponseEntity<ErrorMessage> handleStoreException(StoreException e) {
        StoreException.CODE code = e.getCode();
        HttpStatus status = switch (code) {
            case NO_SUCH_STORE -> HttpStatus.NOT_FOUND;
        };
        String codeStr = code.toString();
        return ResponseEntity
                .status(status)
                .body(new ErrorMessage(codeStr, e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CategoryException.class)
    public ResponseEntity<ErrorMessage> handleCategoryException(CategoryException e) {
        CategoryException.CODE code = e.getCode();
        HttpStatus status = switch (code) {
            case NO_SUCH_CATEGORY -> HttpStatus.NOT_FOUND;
            case NAME_IN_USE -> HttpStatus.CONFLICT;
        };
        String codeStr = code.toString();
        return ResponseEntity
                .status(status)
                .body(new ErrorMessage(codeStr, e.getMessage()));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorMessage> handleProductException(ProductException e) {
        ProductException.CODE code = e.getCode();
        HttpStatus status = switch (code) {
            case NO_SUCH_PRODUCT -> HttpStatus.NOT_FOUND;
            case NAME_IN_USE -> HttpStatus.CONFLICT;
            case TEA_POT -> HttpStatus.I_AM_A_TEAPOT;
        };
        String codeStr = code.toString();
        return ResponseEntity
                .status(status)
                .body(new ErrorMessage(codeStr, e.getMessage()));
    }

}

package com.example.project.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1) 비즈니스 예외
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e, HttpServletRequest request) {

        ErrorCode errorCode = e.getErrorCode();

        log.warn("BusinessException: code={}, message={}", errorCode.getCode(), e.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .code(errorCode.getCode())
                .message(e.getMessage()) // 필요하면 errorCode.getMessage()로 통일
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(errorCode.getStatus())
                .body(response);
    }

    /**
     * 2) @Valid 실패 – RequestBody 검증 에러
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                         HttpServletRequest request) {

        List<ErrorResponse.FieldError> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> ErrorResponse.FieldError.builder()
                        .field(err.getField())
                        .value(err.getRejectedValue() == null ? "" : err.getRejectedValue().toString())
                        .reason(err.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

        ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .errors(fieldErrors)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    /**
     * 3) @RequestParam, @PathVariable 검증 실패 등
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException e,
                                                                      HttpServletRequest request) {

        List<ErrorResponse.FieldError> fieldErrors = e.getConstraintViolations()
                .stream()
                .map(violation -> ErrorResponse.FieldError.builder()
                        .field(violation.getPropertyPath().toString())
                        .value(violation.getInvalidValue() == null ? "" : violation.getInvalidValue().toString())
                        .reason(violation.getMessage())
                        .build())
                .collect(Collectors.toList());

        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

        ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .errors(fieldErrors)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    /**
     * 4) 필수 파라미터 없음
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(
            MissingServletRequestParameterException e,
            HttpServletRequest request) {

        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

        ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .code(errorCode.getCode())
                .message("필수 파라미터가 누락되었습니다: " + e.getParameterName())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    /**
     * 5) 지원하지 않는 HTTP 메서드
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                                     HttpServletRequest request) {

        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;

        ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    /**
     * 6) Spring Security – 인가 실패 (403)
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException e, HttpServletRequest request) {

        ErrorCode errorCode = ErrorCode.AUTH_FORBIDDEN;

        ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    /**
     * 7) 나머지 모든 예외 – 서버 에러 처리 (500)
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        // 예상치 못한 모든 예외 = ERROR + 스택트레이스 출력
        log.error("[UnhandledException] path={}, message={}", request.getRequestURI(), e.getMessage(), e);

        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        ErrorResponse response = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

}

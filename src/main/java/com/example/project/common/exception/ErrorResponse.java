package com.example.project.common.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ErrorResponse {

    private int status;         // HTTP Status Code (예: 400, 401, 403...)
    private String code;        // 시스템 공통 에러 코드 (C001, A001 등)
    private String message;     // 사용자에게 보여줄 메시지
    private String path;        // 요청 URI
    private LocalDateTime timestamp;  // 발생 시각
    private List<FieldError> errors;  // 필드 검증 에러 목록 (nullable)

    @Getter
    @Builder
    public static class FieldError {
        private String field;
        private String value;
        private String reason;
    }

}

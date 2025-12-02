package com.example.project.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    /* ============================
     * 1. COMMON (공통 에러)
     * ============================ */
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "유효하지 않은 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "허용되지 않은 HTTP 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "서버 내부 오류가 발생했습니다."),
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "C004", "요청 JSON 형식이 잘못되었습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "C005", "잘못된 요청입니다."),
    FAILED_UPDATE(HttpStatus.NOT_FOUND, "C006", "수정된 데이터가 없습니다."),
    FAILED_DELETE(HttpStatus.NOT_FOUND, "C007", "삭제된 데이터가 없습니다."),
    FAILED_INSERT(HttpStatus.NOT_FOUND, "C008", "저장된 데이터가 없습니다."),

    /* ============================
     * 2. AUTH (인증/인가 에러)
     * ============================ */
    AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "인증이 필요합니다."),
    AUTH_FORBIDDEN(HttpStatus.FORBIDDEN, "A002", "접근 권한이 없습니다."),
    AUTH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "A003", "유효하지 않은 토큰입니다."),
    AUTH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A004", "토큰이 만료되었습니다."),
    AUTH_TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "A005", "인증 토큰이 존재하지 않습니다."),

    /* ============================
     * 3. USER (회원 관련 에러)
     * ============================ */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "존재하지 않는 사용자입니다."),
    USER_EMAIL_DUPLICATED(HttpStatus.CONFLICT, "U002", "이미 존재하는 이메일입니다."),
    USER_INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "U003", "비밀번호가 일치하지 않습니다."),
    USER_INACTIVE(HttpStatus.FORBIDDEN, "U004", "비활성화된 사용자입니다."),

    /* ============================
     * 4. NETWORK (Wi-Fi 네트워크 관련)
     * ============================ */
    NETWORK_NOT_FOUND(HttpStatus.NOT_FOUND, "N001", "등록된 Wi-Fi 네트워크를 찾을 수 없습니다."),
    NETWORK_INACTIVE(HttpStatus.GONE, "N002", "비활성화된 Wi-Fi 네트워크입니다."),
    NETWORK_NO_PERMISSION(HttpStatus.FORBIDDEN, "N003", "해당 Wi-Fi 네트워크에 대한 권한이 없습니다."),
    NETWORK_PASSWORD_DECRYPT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "N004", "Wi-Fi 비밀번호 복호화에 실패했습니다."),
    NETWORK_PASSWORD_ENCRYPT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "N005", "Wi-Fi 비밀번호 암호화에 실패했습니다."),

    /* ============================
     * 5. QR CODE (Wi-Fi QR 관련)
     * ============================ */
    QR_NOT_FOUND(HttpStatus.NOT_FOUND, "Q001", "QR 코드를 찾을 수 없습니다."),
    QR_INACTIVE(HttpStatus.GONE, "Q002", "비활성화된 QR 코드입니다."),
    QR_EXPIRED(HttpStatus.GONE, "Q003", "만료된 QR 코드입니다."),
    QR_NO_PERMISSION(HttpStatus.FORBIDDEN, "Q004", "해당 QR 코드에 대한 권한이 없습니다."),
    QR_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Q005", "QR 코드 생성에 실패했습니다."),
    QR_DECRYPT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Q006", "QR 데이터 복호화에 실패했습니다."),
    QR_TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "Q007", "QR 생성 요청이 너무 많습니다."),

    /* ============================
     * 6. FILE & STORAGE (QR 이미지 저장 관련)
     * ============================ */
    FILE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "F001", "파일 저장에 실패했습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "F002", "파일을 찾을 수 없습니다."),
    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "F003", "파일 삭제에 실패했습니다."),

    /* ============================
     * 7. SECURITY (암호화/복호화)
     * ============================ */
    ENCRYPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "암호화 과정에서 오류가 발생했습니다."),
    DECRYPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S002", "복호화 과정에서 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}

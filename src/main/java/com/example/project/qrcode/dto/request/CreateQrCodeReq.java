package com.example.project.qrcode.dto.request;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(description = "네트워크 등록")
public class CreateQrCodeReq {

    @Schema(description = "네트워크 고유번호", example = "1")
    @NotNull
    private Long networkSeq;

    @Schema(description = "qr코드 고유번호") //qr코드 생성 후 반환 받는 용도로 사용
    @Hidden
    private Long qrCodeSeq;

}

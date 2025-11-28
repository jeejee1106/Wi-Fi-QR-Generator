package com.example.project.qrcode.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "QR 코드 비활성화")
public class DeactivateQrCodeReq {

    @Schema(description = "만료하는 이유", example = "더 이상 사용안함")
    private String deactivatedReason;


}

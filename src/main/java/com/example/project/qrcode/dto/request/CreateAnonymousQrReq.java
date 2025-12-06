package com.example.project.qrcode.dto.request;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Schema(description = "QR 코드 생성")
public class CreateAnonymousQrReq {

    @Schema(description = "ssid", example = "1234")
    @NotNull
    private String ssid;

    @Schema(description = "네트워크 비밀번호", example = "1234")
    @Setter
    private String password;

    @Schema(description = "보안 타입", example = "WPA") // WPA, WEP, NOPASS
    @NotNull
    private String authType;

    @Schema(description = "숨김여부", example = "N")
    private String hiddenYn;

    @Schema(description = "사용자 지정 네트워크 별칭")
    private String alias;

    @Schema(description = "메모")
    private String memo;

    @Schema(description = "사용여부", example = "Y")
    private String activeYn;

    @Schema(description = "네트워크 고유번호") //네트워크 등록 후 반환 받을 때 사용
    @Hidden
    private Long networkSeq;

    @Schema(description = "qr코드 고유번호") //qr코드 생성 후 반환 받는 용도로 사용
    @Hidden
    private Long qrCodeSeq;

}

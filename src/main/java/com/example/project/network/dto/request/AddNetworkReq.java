package com.example.project.network.dto.request;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "네트워크 등록")
public class AddNetworkReq {

    @Schema(description = "유저 고유번호", example = "1")
    @NotNull
    private Long userSeq;

    @Schema(description = "ssid", example = "1234")
    @NotNull
    private String ssid;

    @Schema(description = "네트워크 비밀번호", example = "1234")
    private String password;

    @Schema(description = "보안 타입", example = "WPA") // WPA, WEP, NOPASS
    @NotNull
    private String authType;

    @Schema(description = "숨김여부", example = "N")
    private String hiddenYn;

    @Schema(description = "메모")
    private String memo;

    @Schema(description = "사용여부", example = "Y")
    private String activeYn;

    @Schema(description = "네트워크 고유번호") //유저 등록 후 반환 받을 때 사용
    @Hidden
    private Long networkSeq;

}

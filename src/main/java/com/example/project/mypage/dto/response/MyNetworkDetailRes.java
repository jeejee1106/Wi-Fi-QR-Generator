package com.example.project.mypage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "내 네트워크 단일 조회 응답")
public class MyNetworkDetailRes {

    @Schema(description = "네트워크 PK", example = "3")
    private Long networkSeq;

    @Schema(description = "SSID", example = "Home_WiFi_5G")
    private String ssid;

    @Schema(description = "인증 타입 (WPA/WPA2 등)", example = "WPA")
    private String authType;

    @Schema(description = "숨김 여부 (Y/N)", example = "N")
    private String hiddenYn;

    @Schema(description = "메모")
    private String memo;

    @Schema(description = "활성 여부 (Y/N)", example = "Y")
    private String activeYn;

    @Schema(description = "게스트 여부 (Y/N)", example = "Y")
    private String guestYn;

    @Schema(description = "생성 일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일시")
    private LocalDateTime updatedAt;

}

package com.example.project.mypage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Schema(description = "내 네트워크 조회 응답")
public class MyNetworkRes {

    @Schema(description = "네트워크 PK", example = "3")
    private Long networkSeq;

    @Schema(description = "SSID", example = "Home_WiFi_5G")
    private String ssid;


    @Schema(description = "사용자 지정 네트워크 별칭")
    private String alias;

    @Schema(description = "활성 여부 (Y/N)", example = "Y")
    private String activeYn;

    @Schema(description = "생성 일시")
    private LocalDateTime createdAt;

}

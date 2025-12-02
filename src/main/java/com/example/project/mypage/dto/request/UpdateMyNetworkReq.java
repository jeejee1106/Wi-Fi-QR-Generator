package com.example.project.mypage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class UpdateMyNetworkReq {

    @Schema(description = "ssid", example = "1234")
    String ssid;

    @Schema(description = "네트워크 비밀번호", example = "1234")
    @Setter
    String password;

    @Schema(description = "보안 타입", example = "WPA")
    String authType;

    @Schema(description = "숨김여부", example = "N")
    String hiddenYn;

    @Schema(description = "사용자 지정 네트워크 별칭")
    String alias;

    @Schema(description = "메모")
    String memo;

    @Schema(description = "활성 여부")
    String activeYn;

}

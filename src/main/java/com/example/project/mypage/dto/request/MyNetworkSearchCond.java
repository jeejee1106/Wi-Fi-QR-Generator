package com.example.project.mypage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;

@Setter
@Schema(description = "내 네트워크 목록 검색 조건")
public class MyNetworkSearchCond {

    @Schema(description = "SSID 키워드 (부분 일치)", example = "Home")
    private String ssidKeyword;

    @Schema(description = "활성 여부 (Y/N) - 선택", example = "Y")
    private String activeYn;

}
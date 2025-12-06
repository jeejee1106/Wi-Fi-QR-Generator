package com.example.project.mypage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "내 QRCODE 조회 응답")
public class MyQrCodeListRes {

    @Schema(description = "총 개수")
    private int totalCount;

    @Schema(description = "QRCODE 목록")
    private List<MyQrCodeRes> list;

}

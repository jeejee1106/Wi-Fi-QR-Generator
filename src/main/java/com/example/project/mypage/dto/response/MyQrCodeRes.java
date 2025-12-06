package com.example.project.mypage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Schema(description = "내 네트워크 조회 응답")
public class MyQrCodeRes {

    private Long qrCodeSeq;
    private Long networkSeq;
    private String qrContent;
    private LocalDate expiresAt;
    private String activeYn;
    private LocalDateTime deactivatedAt;

}

package com.example.project.qrcode.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "qrCode 검색 조건")
public class QrCodeSearchCond {

    private Long qrCodeSeq;
    private Long networkSeq;
    private String activeYn;

}

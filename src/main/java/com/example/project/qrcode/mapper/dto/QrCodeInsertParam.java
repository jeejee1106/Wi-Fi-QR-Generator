package com.example.project.qrcode.mapper.dto;

import lombok.Getter;

@Getter
public class QrCodeInsertParam {

    private final Long networkSeq;
    private Long qrCodeSeq; // generated key

    public QrCodeInsertParam(Long networkSeq) {
        this.networkSeq = networkSeq;
    }
}

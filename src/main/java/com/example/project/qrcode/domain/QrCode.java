package com.example.project.qrcode.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QrCode {

    private Long qrCodeSeq;
    private Long networkSeq;
    private String qrContent;
    private String imagePath;
    private LocalDateTime expiresAt;
    private String activeYn;
    private LocalDateTime createdAt;

}

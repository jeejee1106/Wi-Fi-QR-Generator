package com.example.project.qrcode.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateQrCodeRes {

    private Long qrCodeSeq;
    private Long networkSeq;
    private String qrContent;
    private String imagePath;
    private LocalDate expiresAt;
    private String guestYn;

    public static CreateQrCodeRes of(Long networkSeq,
                                     String qrContent,
                                     String imagePath,
                                     LocalDate expiresAt,
                                     String guestYn) {
        CreateQrCodeRes res = new CreateQrCodeRes();
        res.networkSeq = networkSeq;
        res.qrContent  = qrContent;
        res.imagePath  = imagePath;
        res.expiresAt  = expiresAt;
        res.guestYn    = guestYn;
        return res;
    }


}

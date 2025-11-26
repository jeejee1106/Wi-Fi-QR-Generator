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
    private Long NetworkSeq;
    private String qrContent;
    private String imagePath;   // /qr-images/xxx.png 같은 경로
    private LocalDate expiresAt;

    public static CreateQrCodeRes of(Long qrCodeSeq,
                                     Long networkSeq,
                                     String qrContent,
                                     String imagePath,
                                     LocalDate expiresAt) {
        CreateQrCodeRes res = new CreateQrCodeRes();
        res.qrCodeSeq = qrCodeSeq;
        res.NetworkSeq = networkSeq;
        res.qrContent = qrContent;
        res.imagePath = imagePath;
        res.expiresAt = expiresAt;
        return res;
    }


}

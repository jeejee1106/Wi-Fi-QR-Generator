package com.example.project.qrcode.service;

import com.example.project.qrcode.dto.request.CreateAnonymousQrReq;
import com.example.project.qrcode.dto.request.CreateQrCodeReq;
import com.example.project.qrcode.dto.request.DeactivateQrCodeReq;
import com.example.project.qrcode.dto.response.CreateQrCodeRes;
import com.example.project.qrcode.dto.response.WifiConnectRes;

public interface QrCodeService {

    CreateQrCodeRes createQrCode(CreateQrCodeReq req);
    CreateQrCodeRes createQrCodeWithUrlContent(CreateQrCodeReq req);
    CreateQrCodeRes createAnonymousQrCode(CreateAnonymousQrReq req);
    WifiConnectRes scanWifiQr(Long qrCodeSeq);
    void deactivateQrCode(Long qrCodeSeq, DeactivateQrCodeReq req, Long userSeq);

}

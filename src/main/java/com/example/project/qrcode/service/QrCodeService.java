package com.example.project.qrcode.service;

import com.example.project.qrcode.dto.request.CreateQrCodeReq;
import com.example.project.qrcode.dto.response.CreateQrCodeRes;

public interface QrCodeService {

    CreateQrCodeRes createQrCode(CreateQrCodeReq req);

}

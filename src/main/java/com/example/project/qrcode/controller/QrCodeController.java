package com.example.project.qrcode.controller;

import com.example.project.qrcode.dto.request.CreateQrCodeReq;
import com.example.project.qrcode.dto.response.CreateQrCodeRes;
import com.example.project.qrcode.service.QrCodeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Tag(name = "QR-CODE API", description = "QR CODE 관리 API")
@RequestMapping("/qr")
public class QrCodeController {

    private final QrCodeService qrCodeService;

    @PostMapping()
    public ResponseEntity<CreateQrCodeRes> createQrCode(@RequestBody CreateQrCodeReq req) {
        CreateQrCodeRes res = qrCodeService.createQrCode(req);

        return ResponseEntity
                .created(URI.create("/api/qr-codes/"))
                .body(res);
    }

}

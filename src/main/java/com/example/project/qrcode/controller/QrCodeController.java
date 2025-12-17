package com.example.project.qrcode.controller;

import com.example.project.qrcode.dto.request.CreateAnonymousQrReq;
import com.example.project.qrcode.dto.request.CreateQrCodeReq;
import com.example.project.qrcode.dto.response.CreateQrCodeRes;
import com.example.project.qrcode.dto.response.WifiConnectRes;
import com.example.project.qrcode.service.QrCodeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Tag(name = "QR-CODE API", description = "QR CODE 관리 API")
@RequestMapping("/qr")
public class QrCodeController {

    private final QrCodeService qrCodeService;

    @PostMapping("/{networkSeq}")
    public ResponseEntity<CreateQrCodeRes> createQrCode(@PathVariable Long networkSeq, @Valid @RequestBody CreateQrCodeReq req) {
        CreateQrCodeRes res = qrCodeService.createQrCode(networkSeq, req);

        return ResponseEntity
                .created(URI.create("/api/qr-codes/"))
                .body(res);
    }

    //위는 와이파이 주소 자체로 qr 생성. 이건 url로 qr 생성
    @PostMapping("/{networkSeq}/url")
    public ResponseEntity<CreateQrCodeRes> createQrCodeWithUrlContent(@PathVariable Long networkSeq, @Valid @RequestBody CreateQrCodeReq req) {
        CreateQrCodeRes res = qrCodeService.createQrCodeWithUrlContent(networkSeq, req);

        return ResponseEntity
                .created(URI.create("/api/qr-codes/"))
                .body(res);
    }

    //위 /url 컨트롤러를 타고 qr을 만들었고, 그 qr을 찍으면 들어올 컨트롤러임.
    //QR ID로 DB에서 Wi-Fi 정보를 조회한다. 조회한 정보를 클라이언트에게 보내준다.
    @GetMapping("/url/{qrCodeSeq}")
    public ResponseEntity<WifiConnectRes> scanWifiQr(@PathVariable Long qrCodeSeq) {
        WifiConnectRes res = qrCodeService.scanWifiQr(qrCodeSeq);
        return ResponseEntity.ok(res);
    }

    //비로그인 전용 wifi-qr 생성
    @PostMapping("/anonymous")
    public ResponseEntity<CreateQrCodeRes> createAnonymousQr(@Valid @RequestBody CreateAnonymousQrReq req) {
        CreateQrCodeRes res = qrCodeService.createAnonymousQrCode(req);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }

}

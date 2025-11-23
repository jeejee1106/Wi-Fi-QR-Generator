package com.example.project.qrcode.service;

import com.example.project.common.security.crypto.WifiPasswordEncryptor;
import com.example.project.common.util.QrImageGenerator;
import com.example.project.common.util.WifiQrContentBuilder;
import com.example.project.network.dto.response.AddNetworkRes;
import com.example.project.network.mapper.NetworkMapper;
import com.example.project.qrcode.dto.request.CreateQrCodeReq;
import com.example.project.qrcode.dto.response.CreateQrCodeRes;
import com.example.project.qrcode.mapper.QrCodeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class QrCodeServiceImpl implements QrCodeService {

    private final QrCodeMapper  qrCodeMapper;
    private final NetworkMapper networkMapper;
    private final WifiPasswordEncryptor wifiPasswordEncryptor;

    @Value("${qr.image.dir}")
    private String qrImageOutputDir;

    @Override
    public CreateQrCodeRes createQrCode(CreateQrCodeReq req) {

        AddNetworkRes networkRes = networkMapper.getNetworkById(req);

        //1. 암호화 pass, 복호화 pass 둘 다 변수에 저장
        String encryptedPw = networkRes.getPassword(); //암호화 password
        String decryptedPw = wifiPasswordEncryptor.decrypt(networkRes.getPassword()); //복호화 password

        String ssid = networkRes.getSsid();
        String authType = networkRes.getAuthType();
        String hiddenYn = networkRes.getHiddenYn();

        //2. QR 내용 문자열 생성
        String plainQrContent = WifiQrContentBuilder.build(
                ssid,
                decryptedPw,
                authType,
                hiddenYn
        );

        //3. QR PNG 생성
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String fileName = "wifi_" + ssid + "_" + now;
        String imagePath = QrImageGenerator.generatePng(plainQrContent, qrImageOutputDir, fileName);


        //4. DB 저장용 QR 콘텐츠 (다시 암호화된 비밀번호로)
        String dbQrContent = WifiQrContentBuilder.build(
                ssid,
                encryptedPw,
                authType,
                hiddenYn
        );

        //5. DB 저장
        CreateQrCodeRes createQrCodeRes = CreateQrCodeRes.of(
                req.getQrCodeSeq(),
                req.getNetworkSeq(),
                dbQrContent,// DB에는 암호문 버전 저장
                imagePath,
                null
        );

        qrCodeMapper.createQrCode(createQrCodeRes);

        return createQrCodeRes;
    }
}

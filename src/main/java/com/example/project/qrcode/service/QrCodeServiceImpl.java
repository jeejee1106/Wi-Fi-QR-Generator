package com.example.project.qrcode.service;

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

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class QrCodeServiceImpl implements QrCodeService {

    private final QrCodeMapper  qrCodeMapper;
    private final NetworkMapper networkMapper;

    @Value("${qr.image.dir}")
    private String qrImageOutputDir;

    @Override
    public CreateQrCodeRes createQrCode(CreateQrCodeReq req) {

        AddNetworkRes networkRes = networkMapper.getNetworkById(req);

        // 1) QR 내용 문자열 생성
        String qrContent = WifiQrContentBuilder.build(
                networkRes.getSsid(),
                networkRes.getPassword(),
                networkRes.getAuthType(),
                networkRes.getHiddenYn()
        );

        // 2) QR PNG 생성
        String fileName = "wifi_" + networkRes.getSsid() + "_" + System.currentTimeMillis();
        String imagePath = QrImageGenerator.generatePng(qrContent, qrImageOutputDir, fileName);

        // 3) DB 저장
        CreateQrCodeRes createQrCodeRes = CreateQrCodeRes.of(
                req.getQrCodeSeq(),
                req.getNetworkSeq(),
                qrContent,
                imagePath,
                null);
        qrCodeMapper.createQrCode(createQrCodeRes);

        return createQrCodeRes;
    }
}

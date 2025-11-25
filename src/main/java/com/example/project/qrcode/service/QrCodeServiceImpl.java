package com.example.project.qrcode.service;

import com.example.project.common.security.crypto.WifiPasswordEncryptor;
import com.example.project.common.util.QrImageGenerator;
import com.example.project.common.util.WifiQrContentBuilder;
import com.example.project.network.dto.response.AddNetworkRes;
import com.example.project.network.mapper.NetworkMapper;
import com.example.project.qrcode.domain.QrCode;
import com.example.project.qrcode.dto.request.CreateQrCodeReq;
import com.example.project.qrcode.dto.response.CreateQrCodeRes;
import com.example.project.qrcode.dto.response.WifiConnectRes;
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

    @Value("${qr.base.url}")
    private String qrBaseUrl;

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

    public CreateQrCodeRes createQrCodeWithUrlContent(CreateQrCodeReq req) {

        // 1. 네트워크 정보 조회
        AddNetworkRes networkRes = networkMapper.getNetworkById(req);
        String ssid = networkRes.getSsid();

        // 2. 먼저 QR row를 INSERT 해서 시퀀스 번호 생성
        qrCodeMapper.insertEmptyQrCode(req);
        Long qrCodeSeq = req.getQrCodeSeq();

        // 3. URL 생성
        String qrUrl = qrBaseUrl + "/qr/url/" + qrCodeSeq;

        // 4. QR 이미지 생성
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String fileName = "wifi_" + ssid + "_" + now;
        String imagePath = QrImageGenerator.generatePng(qrUrl, qrImageOutputDir, fileName);

        // 5. URL, 이미지 경로 업데이트
        qrCodeMapper.updateQrContentAndImagePath(qrCodeSeq, qrUrl, imagePath);

        // 6. 반환
        return new CreateQrCodeRes(
                qrCodeSeq,
                req.getNetworkSeq(),
                qrUrl,
                imagePath,
                null
        );
    }

    public WifiConnectRes getWifiInfoByQrCodeSeq(Long qrCodeSeq) {

        // 1. QR 코드 조회
        QrCode qrCode = qrCodeMapper.findByQrCodeSeq(qrCodeSeq);
        if (qrCode == null) {
            throw new IllegalArgumentException("QR 코드가 존재하지 않습니다. qrCodeSeq=" + qrCodeSeq);
        }

        // 2. 연결된 네트워크 조회
        Long networkSeq = qrCode.getNetworkSeq();
        AddNetworkRes networkRes = networkMapper.getNetworkById(networkSeq);
        if (networkRes == null) {
            throw new IllegalStateException("연결된 Wi-Fi 네트워크가 존재하지 않습니다. networkSeq=" + networkSeq);
        }

        // 3. 비밀번호 복호화
        String decryptedPw = wifiPasswordEncryptor.decrypt(networkRes.getPassword());

        // 4. 응답 DTO 생성
        return WifiConnectRes.builder()
                .ssid(networkRes.getSsid())
                .password(decryptedPw)
                .authType(networkRes.getAuthType())
                .hiddenYn(networkRes.getHiddenYn())
                .build();
    }
}

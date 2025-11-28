package com.example.project.qrcode.service;

import com.example.project.common.exception.BusinessException;
import com.example.project.common.exception.ErrorCode;
import com.example.project.common.security.crypto.WifiPasswordEncryptor;
import com.example.project.common.util.QrImageGenerator;
import com.example.project.common.util.WifiQrContentBuilder;
import com.example.project.network.dto.request.AddNetworkReq;
import com.example.project.network.dto.response.AddNetworkRes;
import com.example.project.network.mapper.NetworkMapper;
import com.example.project.qrcode.domain.QrCode;
import com.example.project.qrcode.dto.request.CreateAnonymousQrReq;
import com.example.project.qrcode.dto.request.CreateQrCodeReq;
import com.example.project.qrcode.dto.response.CreateQrCodeRes;
import com.example.project.qrcode.dto.response.WifiConnectRes;
import com.example.project.qrcode.mapper.QrCodeMapper;
import com.example.project.qrcode.service.dto.QrContentBundle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

        //3. qr생성 및 db용 qrContents 생성
        QrContentBundle qrContentBundle = generateQrCodeAndQrContents(ssid, decryptedPw, encryptedPw, authType, hiddenYn);

        //4. DB 저장
        CreateQrCodeRes createQrCodeRes = CreateQrCodeRes.of(
                req.getNetworkSeq(),
                qrContentBundle.getQrContent(),// DB에는 암호문 버전 저장
                qrContentBundle.getImagePath(),
                req.getExpiresAt(),
                "N"
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

        // 5. QR정보 UPDATE
        qrCodeMapper.updateQrInfo(qrCodeSeq, qrUrl, imagePath, req.getExpiresAt());

        // 6. 반환
        return new CreateQrCodeRes(
                qrCodeSeq,
                req.getNetworkSeq(),
                qrUrl,
                imagePath,
                req.getExpiresAt(),
                "N"
        );
    }

    public WifiConnectRes scanWifiQr(Long qrCodeSeq) {

        //1. QR 코드 조회
        QrCode qrCode = qrCodeMapper.findByQrCodeSeq(qrCodeSeq);
        if (qrCode == null) {
            throw new BusinessException(ErrorCode.QR_NOT_FOUND); //404
        }

        //2. 비활성 QR 체크
        if (!"Y".equals(qrCode.getActiveYn())) {
            throw new BusinessException(ErrorCode.QR_INACTIVE); //410
        }

        //3. 만료시간 체크
        LocalDate now = LocalDate.now();
        LocalDate expiresAt = qrCode.getExpiresAt();
        if (qrCode.getExpiresAt() != null && !expiresAt.isAfter(now)) {
            throw new BusinessException(ErrorCode.QR_EXPIRED); //410
        }

        //4. 연결된 네트워크 조회
        Long networkSeq = qrCode.getNetworkSeq();
        AddNetworkRes networkRes = networkMapper.getNetworkById(networkSeq);
        if (networkRes == null) {
            throw new BusinessException(ErrorCode.NETWORK_NOT_FOUND); //404
        }

        //5. 비밀번호 복호화
        String decryptedPw = wifiPasswordEncryptor.decrypt(networkRes.getPassword());

        //6. 응답 DTO 생성
        return WifiConnectRes.builder()
                .ssid(networkRes.getSsid())
                .password(decryptedPw)
                .authType(networkRes.getAuthType())
                .hiddenYn(networkRes.getHiddenYn())
                .build();
    }

    public CreateQrCodeRes createAnonymousQrCode(CreateAnonymousQrReq req) {

        //1. 평문 pass, 암호화 pass 둘 다 변수에 저장
        String plainPw = req.getPassword(); //평문 password
        String encryptedPw = wifiPasswordEncryptor.encrypt(req.getPassword()); //암호화 password

        String ssid = req.getSsid();
        String authType = req.getAuthType();
        String hiddenYn = req.getHiddenYn();

        //2. 네트워크 저장 (게스트 플래그 Y)
        AddNetworkReq network = AddNetworkReq.of(
                null,
                ssid,
                encryptedPw,
                authType,
                hiddenYn,
                req.getMemo(),
                req.getActiveYn(),
                "Y");
        networkMapper.addNetwork(network);

        //3. qr생성 및 db용 qrContents 생성
        QrContentBundle qrContentBundle = generateQrCodeAndQrContents(ssid, plainPw, encryptedPw, authType, hiddenYn);

        //4. DB 저장
        CreateQrCodeRes createQrCodeRes = CreateQrCodeRes.of(
                network.getNetworkSeq(),
                qrContentBundle.getQrContent(),// DB에는 암호문 버전 저장
                qrContentBundle.getImagePath(),
                req.getExpiresAt(),
                "Y"
        );
        qrCodeMapper.createQrCode(createQrCodeRes);

        return createQrCodeRes;
    }

    public QrContentBundle generateQrCodeAndQrContents(String ssid,
                                String plainPw,
                                String encryptedPw,
                                String authType,
                                String hiddenYn) {
        //1. QR 내용 문자열 생성
        String plainQrContent = WifiQrContentBuilder.build(
                ssid,
                plainPw, //평문 비밀번호
                authType,
                hiddenYn
        );

        //2. QR PNG 생성
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String fileName = "wifi_" + ssid + "_" + now;
        String imagePath = QrImageGenerator.generatePng(plainQrContent, qrImageOutputDir, fileName);

        //3. DB 저장용 QR 콘텐츠 (다시 암호화된 비밀번호로)
        String dbQrContent = WifiQrContentBuilder.build(
                ssid,
                encryptedPw,
                authType,
                hiddenYn
        );

        return new QrContentBundle(dbQrContent, imagePath);
    }


    public void deactivateQr(Long qrCodeSeq, Long userSeq) {

        //1. QR 코드 조회
        QrCode qrCode = qrCodeMapper.findByQrCodeSeq(qrCodeSeq);
        if (qrCode == null) {
            throw new BusinessException(ErrorCode.QR_NOT_FOUND); //404
        }

        //2. QR에서 networkSeq 가져옴
        Long networkSeq = qrCode.getNetworkSeq();
        if (networkSeq == null) {
            throw new BusinessException(ErrorCode.NETWORK_NOT_FOUND); //404
        }

        //3. 네트워크 정보 조회
        AddNetworkRes network = networkMapper.getNetworkById(networkSeq);
        if (network == null) {
            throw new BusinessException(ErrorCode.NETWORK_NOT_FOUND); //404
        }

        //4. 권한 체크: 네트워크 소유자와 현재 로그인한 user 비교
        if (!network.getUserSeq().equals(userSeq)) {
            throw new BusinessException(ErrorCode.AUTH_FORBIDDEN); //403
        }

        //5. active_yn UPDATE (비활성화)
        qrCodeMapper.deactivate(qrCodeSeq);
    }
}

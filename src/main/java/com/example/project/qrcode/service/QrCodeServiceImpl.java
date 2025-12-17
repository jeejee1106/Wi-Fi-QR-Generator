package com.example.project.qrcode.service;

import com.example.project.common.exception.BusinessException;
import com.example.project.common.exception.ErrorCode;
import com.example.project.common.security.crypto.WifiPasswordEncryptor;
import com.example.project.common.util.QrImageGenerator;
import com.example.project.common.util.WifiQrContentBuilder;
import com.example.project.network.dto.request.AddNetworkReq;
import com.example.project.network.mapper.NetworkMapper;
import com.example.project.qrcode.domain.QrCode;
import com.example.project.qrcode.dto.request.CreateAnonymousQrReq;
import com.example.project.qrcode.dto.request.CreateQrCodeReq;
import com.example.project.qrcode.dto.request.QrCodeSearchCond;
import com.example.project.qrcode.dto.response.CreateQrCodeRes;
import com.example.project.qrcode.dto.response.WifiConnectRes;
import com.example.project.qrcode.mapper.QrCodeMapper;
import com.example.project.qrcode.mapper.dto.QrCodeInsertParam;
import com.example.project.qrcode.service.dto.QrContentBundle;
import com.example.project.qrcode.service.dto.QrNetworkInfo;
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
@Transactional(readOnly = true)
public class QrCodeServiceImpl implements QrCodeService {

    private final QrCodeMapper  qrCodeMapper;
    private final NetworkMapper networkMapper;
    private final WifiPasswordEncryptor wifiPasswordEncryptor;

    @Value("${qr.image.dir}")
    private String qrImageOutputDir;

    @Value("${qr.base.url}")
    private String qrBaseUrl;

    @Override
    @Transactional
    public CreateQrCodeRes createQrCode(Long networkSeq, CreateQrCodeReq req) {

        QrNetworkInfo networkInfo = networkMapper.getNetworkById(networkSeq);

        if (networkInfo == null) {
            throw new BusinessException(ErrorCode.NETWORK_NOT_FOUND);
        }

        //1. 비밀번호 여부 체크 및 암복호화 저장
        String encryptedPw = networkInfo.getPassword(); //암호화 password
        boolean hasPassword = encryptedPw != null && !encryptedPw.isBlank();
        String decryptedPw = hasPassword ? wifiPasswordEncryptor.decrypt(encryptedPw) : null; //복호화 password

        String ssid = networkInfo.getSsid();
        String authType = hasPassword ? networkInfo.getAuthType() : "nopass";
        String hiddenYn = networkInfo.getHiddenYn();

        //3. qr생성 및 db용 qrContents 생성
        QrContentBundle qrContentBundle = generateQrCodeAndQrContents(ssid, decryptedPw, encryptedPw, authType, hiddenYn);

        //4. DB 저장
        CreateQrCodeRes createQrCodeRes = CreateQrCodeRes.of(
                networkSeq,
                qrContentBundle.getQrContent(),// DB에는 암호문 버전 저장
                qrContentBundle.getImagePath(),
                req.getExpiresAt(),
                "N"
        );
        qrCodeMapper.createQrCode(createQrCodeRes);

        return createQrCodeRes;
    }

    @Override
    @Transactional
    public CreateQrCodeRes createQrCodeWithUrlContent(Long networkSeq, CreateQrCodeReq req) {

        // 1. 네트워크 정보 조회
        QrNetworkInfo networkInfo = networkMapper.getNetworkById(networkSeq);
        String ssid = networkInfo.getSsid();

        QrCodeInsertParam insertParam = new QrCodeInsertParam(networkSeq);

        // 2. 먼저 QR row를 INSERT 해서 시퀀스 번호 생성
        qrCodeMapper.insertEmptyQrCode(insertParam);
        Long qrCodeSeq = insertParam.getQrCodeSeq();

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
                networkSeq,
                qrUrl,
                imagePath,
                req.getExpiresAt(),
                "N"
        );
    }

    @Override
    @Transactional
    public CreateQrCodeRes createAnonymousQrCode(CreateAnonymousQrReq req) {

        //1. 비밀번호 여부 체크 및 암복화 저장
        String plainPw = req.getPassword(); //평문 password
        boolean hasPassword = plainPw != null && !plainPw.isBlank();
        String encryptedPw = hasPassword ? wifiPasswordEncryptor.encrypt(plainPw) : null; //암호화 password

        String ssid = req.getSsid();
        String authType = hasPassword ? req.getAuthType() : "nopass";
        String hiddenYn = req.getHiddenYn();

        //2. 네트워크 저장 (게스트 플래그 Y)
        AddNetworkReq network = AddNetworkReq.of(
                null,
                ssid,
                encryptedPw,
                authType,
                hiddenYn,
                req.getAlias(),
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
                null,
                "Y"
        );
        qrCodeMapper.createQrCode(createQrCodeRes);

        return createQrCodeRes;
    }

    @Override
    public WifiConnectRes scanWifiQr(Long qrCodeSeq) {

        QrCodeSearchCond  qrCodeSearchCond = new QrCodeSearchCond();
        qrCodeSearchCond.setQrCodeSeq(qrCodeSeq);

        //1. QR 코드 조회
        QrCode qrCode = qrCodeMapper.findQrCode(qrCodeSearchCond);
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
        QrNetworkInfo networkInfo = networkMapper.getNetworkById(networkSeq);
        if (networkInfo == null) {
            throw new BusinessException(ErrorCode.NETWORK_NOT_FOUND); //404
        }

        //5. 비밀번호 복호화
        String decryptedPw = wifiPasswordEncryptor.decrypt(networkInfo.getPassword());

        //6. 응답 DTO 생성
        return WifiConnectRes.builder()
                .ssid(networkInfo.getSsid())
                .password(decryptedPw)
                .authType(networkInfo.getAuthType())
                .hiddenYn(networkInfo.getHiddenYn())
                .build();
    }

    private QrContentBundle generateQrCodeAndQrContents(String ssid,
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
}

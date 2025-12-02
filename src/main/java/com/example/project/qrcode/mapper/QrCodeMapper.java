package com.example.project.qrcode.mapper;

import com.example.project.network.dto.request.AddNetworkReq;
import com.example.project.qrcode.domain.QrCode;
import com.example.project.qrcode.dto.request.CreateQrCodeReq;
import com.example.project.qrcode.dto.request.DeactivateQrCodeReq;
import com.example.project.qrcode.dto.request.QrCodeSearchCond;
import com.example.project.qrcode.dto.response.CreateQrCodeRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface QrCodeMapper {

    void createQrCode(CreateQrCodeRes req);
    void insertEmptyQrCode(CreateQrCodeReq req);
    void updateQrInfo(Long qrCodeSeq, String qrContent, String imagePath, LocalDate expiresAt);
    QrCode findQrCode(QrCodeSearchCond cond);
    void deactivateQrCode(QrCode qrCode); //TODO : 오버로딩..흠.. QrCode로 받지말고 cond를 따로 만들어야하나
    void deactivateQrCode(DeactivateQrCodeReq req);
}

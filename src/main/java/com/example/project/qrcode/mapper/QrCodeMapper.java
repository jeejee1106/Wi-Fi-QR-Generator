package com.example.project.qrcode.mapper;

import com.example.project.qrcode.domain.QrCode;
import com.example.project.qrcode.dto.request.CreateQrCodeReq;
import com.example.project.qrcode.dto.request.QrCodeSearchCond;
import com.example.project.qrcode.dto.response.CreateQrCodeRes;
import com.example.project.qrcode.mapper.dto.QrCodeInsertParam;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;

@Mapper
public interface QrCodeMapper {

    void createQrCode(CreateQrCodeRes req);
    void insertEmptyQrCode(QrCodeInsertParam param);
    void updateQrInfo(Long qrCodeSeq, String qrContent, String imagePath, LocalDate expiresAt);
    QrCode findQrCode(QrCodeSearchCond cond);
}

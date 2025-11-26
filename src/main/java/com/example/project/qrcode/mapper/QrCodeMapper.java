package com.example.project.qrcode.mapper;

import com.example.project.network.dto.request.AddNetworkReq;
import com.example.project.qrcode.domain.QrCode;
import com.example.project.qrcode.dto.request.CreateQrCodeReq;
import com.example.project.qrcode.dto.response.CreateQrCodeRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface QrCodeMapper {

    void createQrCode(CreateQrCodeRes req);
    void insertEmptyQrCode(CreateQrCodeReq req);
    void updateQrInfo(Long qrCodeSeq, String qrContent, String imagePath, LocalDate expiresAt);
    QrCode findByQrCodeSeq(@Param("qrCodeSeq") Long qrCodeSeq);
    void deactivate(Long qrCodeSeq);
}

package com.example.project.qrcode.mapper;

import com.example.project.network.dto.request.AddNetworkReq;
import com.example.project.qrcode.dto.request.CreateQrCodeReq;
import com.example.project.qrcode.dto.response.CreateQrCodeRes;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QrCodeMapper {

    void createQrCode(CreateQrCodeRes req);

}

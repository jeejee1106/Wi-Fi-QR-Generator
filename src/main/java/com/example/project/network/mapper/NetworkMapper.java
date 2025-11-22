package com.example.project.network.mapper;

import com.example.project.network.dto.request.AddNetworkReq;
import com.example.project.network.dto.response.AddNetworkRes;
import com.example.project.qrcode.dto.request.CreateQrCodeReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NetworkMapper {

    void addNetwork(AddNetworkReq req);
    AddNetworkRes getNetworkById(CreateQrCodeReq req);
}

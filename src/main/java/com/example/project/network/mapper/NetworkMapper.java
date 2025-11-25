package com.example.project.network.mapper;

import com.example.project.network.dto.request.AddNetworkReq;
import com.example.project.network.dto.response.AddNetworkRes;
import com.example.project.qrcode.dto.request.CreateQrCodeReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NetworkMapper {

    void addNetwork(AddNetworkReq req);
    AddNetworkRes getNetworkById(CreateQrCodeReq req);
    AddNetworkRes getNetworkById(@Param("networkSeq") Long networkSeq);
}

package com.example.project.network.mapper;

import com.example.project.network.dto.request.AddNetworkReq;
import com.example.project.qrcode.service.dto.QrNetworkInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NetworkMapper {

    void addNetwork(AddNetworkReq req);
    QrNetworkInfo getNetworkById(Long networkSeq);
}

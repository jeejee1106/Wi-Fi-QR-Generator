package com.example.project.network.mapper;

import com.example.project.network.dto.request.AddNetworkReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NetworkMapper {

    void addNetwork(AddNetworkReq req);

}

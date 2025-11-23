package com.example.project.network.service;

import com.example.project.network.dto.request.AddNetworkReq;
import com.example.project.network.dto.response.AddNetworkRes;

public interface NetworkService {

    AddNetworkRes addNetwork(AddNetworkReq req, Long userSeq);

}

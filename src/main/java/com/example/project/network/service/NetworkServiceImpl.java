package com.example.project.network.service;

import com.example.project.network.dto.request.AddNetworkReq;
import com.example.project.network.dto.response.AddNetworkRes;
import com.example.project.network.mapper.NetworkMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class NetworkServiceImpl implements NetworkService {

    private final NetworkMapper networkMapper;

    @Override
    public AddNetworkRes addNetwork(AddNetworkReq req,  Long userSeq) {

        req.setUserSeq(userSeq);
        networkMapper.addNetwork(req);

        AddNetworkRes res = AddNetworkRes.of(req);
        return res;
    }
}

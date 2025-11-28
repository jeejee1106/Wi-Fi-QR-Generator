package com.example.project.network.service;

import com.example.project.common.security.crypto.WifiPasswordEncryptor;
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
@Transactional(readOnly = true)
public class NetworkServiceImpl implements NetworkService {

    private final NetworkMapper networkMapper;
    private final WifiPasswordEncryptor wifiPasswordEncryptor;

    @Override
    @Transactional
    public AddNetworkRes addNetwork(AddNetworkReq req,  Long userSeq) {

        //1. 비밀번호 여부 체크
        boolean hasPassword = req.getPassword() != null && !req.getPassword().isBlank();

        if (!hasPassword) {
            req.setAuthType("nopass");
            req.setPassword(null);
        } else {
            // 있으면 비밀번호 암호화
            String encryptedPassword = wifiPasswordEncryptor.encrypt(req.getPassword());
            req.setPassword(encryptedPassword);
        }

        //2. 토큰에 있는 유저 정보 넣기
        req.setGuestYn("N"); //무조건 로그인해서 들어오기 때문에 "N" 넣어주기
        req.setUserSeq(userSeq);

        //3. 와이파이 정보 저장
        networkMapper.addNetwork(req);


        return AddNetworkRes.of(req);
    }
}

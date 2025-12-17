package com.example.project.mypage.service;

import com.example.project.mypage.dto.request.MyNetworkSearchCond;
import com.example.project.mypage.dto.request.UpdateMyNetworkReq;
import com.example.project.mypage.dto.response.MyNetworkDetailRes;
import com.example.project.mypage.dto.response.MyNetworkListRes;
import com.example.project.mypage.dto.response.MyQrCodeListRes;
import com.example.project.mypage.dto.request.DeactivateQrCodeReq;
import com.example.project.qrcode.dto.request.QrCodeSearchCond;

public interface MyNetworkService {

    MyNetworkListRes getMyNetworkList(MyNetworkSearchCond cond, Long userSeq);
    MyNetworkDetailRes getMyNetwork(Long networkSeq, Long userSeq);
    void updateMyNetwork(Long networkSeq, UpdateMyNetworkReq req,  Long userSeq);
    void deleteMyNetwork(Long networkSeq, Long userSeq);
    MyQrCodeListRes getMyQrCodeListByNetwork(Long networkSeq, QrCodeSearchCond cond, Long userSeq);
    void deactivateQrCode(Long qrCodeSeq, DeactivateQrCodeReq req, Long userSeq);

}

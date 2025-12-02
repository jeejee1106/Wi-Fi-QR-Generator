package com.example.project.mypage.service;

import com.example.project.mypage.dto.request.MyNetworkSearchCond;
import com.example.project.mypage.dto.request.UpdateMyNetworkReq;
import com.example.project.mypage.dto.response.MyNetworkDetailRes;
import com.example.project.mypage.dto.response.MyNetworkListRes;

public interface MyNetworkService {

    MyNetworkListRes getMyNetworkList(MyNetworkSearchCond cond, Long userSeq);
    MyNetworkDetailRes getMyNetwork(Long networkSeq, Long userSeq);
    void updateMyNetwork(Long networkSeq, UpdateMyNetworkReq req,  Long userSeq);

}

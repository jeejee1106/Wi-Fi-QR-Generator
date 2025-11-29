package com.example.project.mypage.service;

import com.example.project.mypage.dto.request.MyNetworkSearchCond;
import com.example.project.mypage.dto.response.MyNetworkListRes;

import java.util.List;

public interface MyNetworkService {

    MyNetworkListRes getMyNetworkList(Long userSeq, MyNetworkSearchCond cond);

}

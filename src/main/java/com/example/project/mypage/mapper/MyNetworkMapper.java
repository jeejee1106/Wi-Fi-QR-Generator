package com.example.project.mypage.mapper;

import com.example.project.mypage.dto.request.MyNetworkSearchCond;
import com.example.project.mypage.dto.request.UpdateMyNetworkReq;
import com.example.project.mypage.dto.response.MyNetworkDetailRes;
import com.example.project.mypage.dto.response.MyNetworkRes;
import com.example.project.mypage.dto.response.MyQrCodeRes;
import com.example.project.qrcode.dto.request.QrCodeSearchCond;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyNetworkMapper {

    List<MyNetworkRes> getMyNetworkList(@Param("cond") MyNetworkSearchCond cond, @Param("userSeq") Long userSeq);
    MyNetworkDetailRes getMyNetwork(@Param("networkSeq") Long networkSeq, @Param("userSeq") Long userSeq);
    int updateMyNetwork(@Param("networkSeq") Long networkSeq, @Param("req") UpdateMyNetworkReq req, @Param("userSeq") Long userSeq);
    int deleteMyNetwork(@Param("networkSeq") Long networkSeq, @Param("userSeq") Long userSeq);
    List<MyQrCodeRes> getMyQrCodeListByNetwork(@Param("networkSeq")Long networkSeq, @Param("cond")QrCodeSearchCond cond, @Param("userSeq") Long userSeq);

}

package com.example.project.mypage.mapper;

import com.example.project.mypage.dto.request.MyNetworkSearchCond;
import com.example.project.mypage.dto.response.MyNetworkDetailRes;
import com.example.project.mypage.dto.response.MyNetworkRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyNetworkMapper {

    List<MyNetworkRes> getMyNetworkList(@Param("cond") MyNetworkSearchCond cond, @Param("userSeq") Long userSeq);
    MyNetworkDetailRes getMyNetwork(@Param("networkSeq") Long networkSeq, @Param("userSeq") Long userSeq);

}

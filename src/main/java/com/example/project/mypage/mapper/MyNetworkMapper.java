package com.example.project.mypage.mapper;

import com.example.project.mypage.dto.request.MyNetworkSearchCond;
import com.example.project.mypage.dto.response.MyNetworkRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyNetworkMapper {

    List<MyNetworkRes> getMyNetworkList(@Param("userSeq") Long userSeq, @Param("cond") MyNetworkSearchCond cond);

}

package com.example.project.mypage.service;

import com.example.project.common.exception.BusinessException;
import com.example.project.common.exception.ErrorCode;
import com.example.project.mypage.dto.request.MyNetworkSearchCond;
import com.example.project.mypage.dto.response.MyNetworkListRes;
import com.example.project.mypage.dto.response.MyNetworkRes;
import com.example.project.mypage.mapper.MyNetworkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyNetworkServiceImpl implements MyNetworkService {

    private final MyNetworkMapper myNetworkMapper;

    @Override
    public MyNetworkListRes getMyNetworkList(Long userSeq, MyNetworkSearchCond cond){
        if (userSeq == null) {
            throw new BusinessException(ErrorCode.AUTH_UNAUTHORIZED);
        }

        List<MyNetworkRes> resultList = myNetworkMapper.getMyNetworkList(userSeq, cond);

        return MyNetworkListRes.builder()
                .totalCount(resultList.size())
                .list(resultList)
                .build();
    }

}

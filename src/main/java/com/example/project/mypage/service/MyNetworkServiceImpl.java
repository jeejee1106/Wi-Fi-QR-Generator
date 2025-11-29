package com.example.project.mypage.service;

import com.example.project.common.exception.BusinessException;
import com.example.project.common.exception.ErrorCode;
import com.example.project.mypage.dto.request.MyNetworkSearchCond;
import com.example.project.mypage.dto.response.MyNetworkDetailRes;
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
    public MyNetworkListRes getMyNetworkList(MyNetworkSearchCond cond, Long userSeq){
        if (userSeq == null) {
            throw new BusinessException(ErrorCode.AUTH_UNAUTHORIZED);
        }

        List<MyNetworkRes> resultList = myNetworkMapper.getMyNetworkList(cond, userSeq);

        return MyNetworkListRes.builder()
                .totalCount(resultList.size())
                .list(resultList)
                .build();
    }

    @Override
    public MyNetworkDetailRes getMyNetwork(Long networkSeq, Long  userSeq) {
        if (userSeq == null) {
            throw new BusinessException(ErrorCode.AUTH_UNAUTHORIZED);
        }

        MyNetworkDetailRes result = myNetworkMapper.getMyNetwork(networkSeq, userSeq);

        if (result == null) {
            // 네트워크가 없거나, 이 사용자의 네트워크가 아닌 경우
            throw new BusinessException(ErrorCode.NETWORK_NOT_FOUND);
        }

        return result;
    }

}

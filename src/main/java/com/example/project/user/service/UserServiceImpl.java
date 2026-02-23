package com.example.project.user.service;

import com.example.project.common.exception.BusinessException;
import com.example.project.common.exception.ErrorCode;
import com.example.project.user.dto.response.GetMyProfileRes;
import com.example.project.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    @Transactional
    public GetMyProfileRes getMyProfile(Long userSeq) {
        GetMyProfileRes res = userMapper.getMyProfile(userSeq);
        if (res == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return res;
    }
}

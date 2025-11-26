package com.example.project.user.service;

import com.example.project.common.exception.BusinessException;
import com.example.project.common.exception.ErrorCode;
import com.example.project.user.dto.request.UserJoinReq;
import com.example.project.user.dto.response.UserJoinRes;
import com.example.project.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserJoinRes userJoin(UserJoinReq req) {

        // 1. 이메일 중복 체크
        int count = userMapper.existsByEmail(req.getEmail());
        if (count > 0) {
            throw new BusinessException(ErrorCode.USER_EMAIL_DUPLICATED);
        }

        // 2. 비밀번호 암호화
        String encodedPw = passwordEncoder.encode(req.getPassword());
        req.setPassword(encodedPw);

        //4. db 저장
        userMapper.insertUser(req);

        return UserJoinRes.of(req);
    }
}

package com.example.project.user.service;

import com.example.project.user.dto.request.UserJoinReq;
import com.example.project.user.dto.response.UserJoinRes;
import com.example.project.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public UserJoinRes userJoin(UserJoinReq req) {
        
        userMapper.insertUser(req);
        return UserJoinRes.success(req.getUserSeq(), req.getEmail());
    }
}

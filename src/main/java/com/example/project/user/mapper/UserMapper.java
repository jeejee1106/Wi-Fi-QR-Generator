package com.example.project.user.mapper;

import com.example.project.user.dto.request.UserJoinReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    int existsByEmail(String email);
    void insertUser(UserJoinReq req);

}

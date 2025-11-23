package com.example.project.user.mapper;

import com.example.project.user.domain.User;
import com.example.project.user.dto.request.UserJoinReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    int existsByEmail(String email);
    void insertUser(UserJoinReq req);
    User findByEmail(@Param("email") String email);
}

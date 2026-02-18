package com.example.project.auth.mapper;

import com.example.project.auth.domain.Auth;
import com.example.project.auth.dto.request.SignInReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthMapper {

    int existsByEmail(String email);
    void insertUser(SignInReq req);
    Auth findByEmail(@Param("email") String email);
}

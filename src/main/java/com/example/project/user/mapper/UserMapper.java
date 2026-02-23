package com.example.project.user.mapper;

import com.example.project.user.dto.response.GetMyProfileRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    GetMyProfileRes getMyProfile(Long userSeq);
}

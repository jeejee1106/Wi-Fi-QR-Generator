package com.example.project.user.service;

import com.example.project.user.dto.response.GetMyProfileRes;

public interface UserService {

    GetMyProfileRes getMyProfile(Long userSeq);

}

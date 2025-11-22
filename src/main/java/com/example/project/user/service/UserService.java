package com.example.project.user.service;

import com.example.project.user.dto.request.UserJoinReq;
import com.example.project.user.dto.response.UserJoinRes;

public interface UserService {

    UserJoinRes userJoin(UserJoinReq req);

}

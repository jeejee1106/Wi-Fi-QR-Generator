package com.example.project.auth.service;

import com.example.project.auth.dto.request.SignInReq;
import com.example.project.auth.dto.response.SignInRes;

public interface AuthService {

    SignInRes userJoin(SignInReq req);

}

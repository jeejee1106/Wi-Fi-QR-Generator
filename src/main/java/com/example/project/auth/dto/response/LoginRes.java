package com.example.project.auth.dto.response;

import com.example.project.auth.dto.request.LoginReq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRes {

    private String accessToken;
    private String tokenType; // "Bearer"

    public static LoginRes of(LoginReq req) {
        LoginRes res = new LoginRes();
        return res;
    }
}

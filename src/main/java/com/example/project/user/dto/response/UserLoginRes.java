package com.example.project.user.dto.response;

import com.example.project.user.dto.request.UserJoinReq;
import com.example.project.user.dto.request.UserLoginReq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRes {

    private String accessToken;
    private String tokenType; // "Bearer"

    public static UserLoginRes of(UserLoginReq req) {
        UserLoginRes res = new UserLoginRes();
        return res;
    }
}

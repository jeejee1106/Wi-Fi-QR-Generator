package com.example.project.user.dto.response;

import com.example.project.user.dto.request.UserJoinReq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRes {

    private Long userSeq;
    private String email;
    private String name;
    private String role;

    public static UserJoinRes of(UserJoinReq req) {
        UserJoinRes res = new UserJoinRes();
        res.userSeq = req.getUserSeq();
        res.email = req.getEmail();
        res.name = req.getName();
        res.role = req.getRole();
        return res;
    }
}

package com.example.project.auth.dto.response;

import com.example.project.auth.dto.request.SignInReq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignInRes {

    private Long userSeq;
    private String email;
    private String name;
    private String role;

    public static SignInRes of(SignInReq req) {
        SignInRes res = new SignInRes();
        res.userSeq = req.getUserSeq();
        res.email = req.getEmail();
        res.name = req.getName();
//        res.role = req.getRole();
        return res;
    }
}

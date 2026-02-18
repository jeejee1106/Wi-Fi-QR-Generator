package com.example.project.auth.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Auth {

    private Long userSeq;
    private String email;
    private String password;
    private String name;
    private String role;
    private String delYn;

}

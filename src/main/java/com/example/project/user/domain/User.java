package com.example.project.user.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private Long userSeq;
    private String email;
    private String password;
    private String name;
    private String role;
    private String delYn;

}

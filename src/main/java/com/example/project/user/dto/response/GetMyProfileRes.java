package com.example.project.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "내 정보 조회")
public class GetMyProfileRes {

    private Integer userSeq;
    private String email;
    private String name;
    private String role;

}

package com.example.project.user.dto.request;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "회원가입")
public class UserJoinReq {

    @Schema(description = "이메일", example = "example@test.com")
    @NotNull
    private String email;

    @Schema(description = "비밀번호", example = "1234")
    @NotNull
    private String password;

    @Schema(description = "이름", example = "이순신")
    @NotNull
    private String name;

    @Schema(description = "유저 고유번호") //유저 등록 후 반환 받을 때 사용
    @Hidden
    private Long userSeq;

}

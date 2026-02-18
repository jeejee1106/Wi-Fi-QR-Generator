package com.example.project.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "로그인")
public class LoginReq {

    @Schema(description = "이메일", example = "example@test.com")
    @NotNull
    private String email;

    @Schema(description = "비밀번호", example = "1234")
    @NotNull
    private String password;

}

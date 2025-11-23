package com.example.project.user.dto.request;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Schema(description = "로그인")
public class UserLoginReq {

    @Schema(description = "이메일", example = "example@test.com")
    @NotNull
    private String email;

    @Schema(description = "비밀번호", example = "1234")
    @NotNull
    private String password;

}

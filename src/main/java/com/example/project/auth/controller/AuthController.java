package com.example.project.auth.controller;

import com.example.project.common.security.JwtTokenProvider;
import com.example.project.auth.dto.request.SignInReq;
import com.example.project.auth.dto.request.LoginReq;
import com.example.project.auth.dto.response.SignInRes;
import com.example.project.auth.dto.response.LoginRes;
import com.example.project.auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "AUTH API", description = "인증 관리 API")
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping()
    public ResponseEntity<SignInRes> userJoin(@Valid @RequestBody SignInReq req) {
        SignInRes res = authService.userJoin(req);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRes> userLogin(@Valid @RequestBody LoginReq req) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword());

        Authentication authentication = authenticationManager.authenticate(authToken); //이 한 줄에서 유저 조회, 비밀번호 비교, 인증된 principal 세팅까지 모두 자동 처리됨.
        String token = jwtTokenProvider.createToken(authentication);

        return ResponseEntity.ok(new LoginRes(token, "Bearer"));
    }

}

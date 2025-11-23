package com.example.project.user.controller;

import com.example.project.common.security.JwtTokenProvider;
import com.example.project.user.dto.request.UserJoinReq;
import com.example.project.user.dto.request.UserLoginReq;
import com.example.project.user.dto.response.UserJoinRes;
import com.example.project.user.dto.response.UserLoginRes;
import com.example.project.user.service.UserService;
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
@Tag(name = "USER API", description = "유저 관리 API")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping()
    public ResponseEntity<UserJoinRes> userJoin(@Valid @RequestBody UserJoinReq req) {
        UserJoinRes res = userService.userJoin(req);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginRes> userLogin(@Valid @RequestBody UserLoginReq req) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword());

        Authentication authentication = authenticationManager.authenticate(authToken); //이 한 줄에서 유저 조회, 비밀번호 비교, 인증된 principal 세팅까지 모두 자동 처리됨.
        String token = jwtTokenProvider.createToken(authentication);

        return ResponseEntity.ok(new UserLoginRes(token, "Bearer"));
    }

}

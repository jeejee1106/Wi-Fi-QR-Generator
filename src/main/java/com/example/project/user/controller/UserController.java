package com.example.project.user.controller;

import com.example.project.common.security.CustomUserDetails;
import com.example.project.user.dto.response.GetMyProfileRes;
import com.example.project.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "USER API", description = "유저 관리 API")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<GetMyProfileRes> getMyProfile(@AuthenticationPrincipal CustomUserDetails user) {

        Long userSeq = user.getUserSeq();
        GetMyProfileRes res = userService.getMyProfile(userSeq);

        return ResponseEntity
                .ok(res);
    }


}

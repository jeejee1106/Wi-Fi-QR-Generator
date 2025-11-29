package com.example.project.mypage.controller;

import com.example.project.common.security.CustomUserDetails;
import com.example.project.mypage.dto.request.MyNetworkSearchCond;
import com.example.project.mypage.dto.response.MyNetworkListRes;
import com.example.project.mypage.service.MyNetworkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my")
public class MyPageController {

    private final MyNetworkService myNetworkService;

    /**
     * 내가 등록한 Wi-Fi 네트워크 목록 조회
     */
    @GetMapping("/networks")
    public MyNetworkListRes getMyNetworkList(@AuthenticationPrincipal CustomUserDetails user,
                                             @Valid MyNetworkSearchCond cond
    ) {
        Long userSeq = user.getUserSeq();
        return myNetworkService.getMyNetworkList(userSeq, cond);
    }



}



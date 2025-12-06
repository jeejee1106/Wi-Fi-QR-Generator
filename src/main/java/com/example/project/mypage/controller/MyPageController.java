package com.example.project.mypage.controller;

import com.example.project.common.security.CustomUserDetails;
import com.example.project.mypage.dto.request.MyNetworkSearchCond;
import com.example.project.mypage.dto.request.UpdateMyNetworkReq;
import com.example.project.mypage.dto.response.MyNetworkDetailRes;
import com.example.project.mypage.dto.response.MyNetworkListRes;
import com.example.project.mypage.dto.response.MyNetworkRes;
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
    public MyNetworkListRes getMyNetworkList(@Valid MyNetworkSearchCond cond,
                                             @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long userSeq = user.getUserSeq();
        return myNetworkService.getMyNetworkList(cond, userSeq);
    }

    /**
     * 내 네트워크 단건 조회
     */
    @GetMapping("/networks/{networkSeq}")
    public MyNetworkDetailRes getMyNetwork(@PathVariable Long networkSeq,
                                           @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long userSeq = user.getUserSeq();
        return myNetworkService.getMyNetwork(networkSeq, userSeq);
    }

    /**
     * 내 네트워크 수정
     */
    @PatchMapping("/networks/{networkSeq}")
    public void updateMyNetwork(@PathVariable Long networkSeq,
                                @RequestBody @Valid UpdateMyNetworkReq req,
                                @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long userSeq = user.getUserSeq();
        myNetworkService.updateMyNetwork(networkSeq, req, userSeq);
    }

    /**
     * 내 네트워크 삭제
     */
    @DeleteMapping("/networks/{networkSeq}")
    public void deleteMyNetwork(@PathVariable Long networkSeq,
                                @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long userSeq = user.getUserSeq();
        myNetworkService.deleteMyNetwork(networkSeq, userSeq);
    }

}



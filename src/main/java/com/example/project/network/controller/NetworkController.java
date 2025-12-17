package com.example.project.network.controller;

import com.example.project.common.security.CustomUserDetails;
import com.example.project.network.dto.request.AddNetworkReq;
import com.example.project.network.dto.response.AddNetworkRes;
import com.example.project.network.service.NetworkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "NETWORK API", description = "네트워크 관리 API")
@RequestMapping("/network")
public class NetworkController {

    private final NetworkService networkService;

    @PostMapping()
    public ResponseEntity<AddNetworkRes> addNetwork(@Valid @RequestBody AddNetworkReq req,
                                                    @AuthenticationPrincipal CustomUserDetails user) {

        Long userSeq = user.getUserSeq();
        AddNetworkRes res = networkService.addNetwork(req, userSeq);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }

}

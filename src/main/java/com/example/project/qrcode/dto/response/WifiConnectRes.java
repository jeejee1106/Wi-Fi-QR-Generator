package com.example.project.qrcode.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WifiConnectRes {

    private String ssid;
    private String password;
    private String authType;
    private String hiddenYn;

}

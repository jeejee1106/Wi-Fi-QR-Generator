package com.example.project.qrcode.service.dto;

import lombok.Getter;

@Getter
public class QrNetworkInfo {

    private Long userSeq;
    private String ssid;
    private String password;
    private String authType;
    private String hiddenYn;
    private String alias;
    private String memo;
    private String activeYn;
    private Long networkSeq;

}

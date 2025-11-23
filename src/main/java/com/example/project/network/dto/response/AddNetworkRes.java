package com.example.project.network.dto.response;

import com.example.project.network.dto.request.AddNetworkReq;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddNetworkRes {

    private Long userSeq;
    private String ssid;
    @Setter
    private String password;
    private String authType;
    private String hiddenYn;
    private String memo;
    private String activeYn;
    private Long networkSeq;

    public static AddNetworkRes of(AddNetworkReq req) {
        AddNetworkRes res = new AddNetworkRes();
        res.userSeq = req.getUserSeq();
        res.ssid = req.getSsid();
        res.password = req.getPassword();
        res.authType = req.getAuthType();
        res.hiddenYn = req.getHiddenYn();
        res.memo = req.getMemo();
        res.activeYn = req.getActiveYn();
        res.networkSeq = req.getNetworkSeq();
        return res;
    }


}

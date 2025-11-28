package com.example.project.network.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Schema(description = "네트워크 등록")
public class AddNetworkReq {

    @Schema(description = "유저 고유번호", example = "1")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Setter
    private Long userSeq;

    @Schema(description = "ssid", example = "1234")
    @NotNull
    private String ssid;

    @Schema(description = "네트워크 비밀번호", example = "1234")
    @Setter
    private String password;

    @Schema(description = "보안 타입", example = "WPA") // WPA, WEP, NOPASS
    @Setter
    @NotNull
    private String authType;

    @Schema(description = "숨김여부", example = "N")
    private String hiddenYn;

    @Schema(description = "메모")
    private String memo;

    @Schema(description = "사용여부", example = "Y")
    private String activeYn;

    @Schema(description = "게스트(비회원) 여부", example = "Y")
    @Setter
    private String guestYn;

    @Schema(description = "네트워크 고유번호") //네트워크 등록 후 반환 받을 때 사용
    @Hidden
    private Long networkSeq;

    public static AddNetworkReq of(Long userSeq,
                                   String ssid,
                                   String password,
                                   String authType,
                                   String hiddenYn,
                                   String memo,
                                   String activeYn,
                                   String guestYn) {
        AddNetworkReq res = new AddNetworkReq();
        res.userSeq  = userSeq;
        res.ssid     = ssid;
        res.password = password;
        res.authType = authType;
        res.hiddenYn = hiddenYn;
        res.memo     = memo;
        res.activeYn = activeYn;
        res.guestYn = guestYn;
        return res;
    }

}

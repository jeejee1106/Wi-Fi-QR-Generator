package com.example.project.mypage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "내 네트워크 조회 응답")
public class MyNetworkListRes {

        @Schema(description = "총 개수")
        private int totalCount;

        @Schema(description = "네트워크 목록")
        private List<MyNetworkRes> list;

}

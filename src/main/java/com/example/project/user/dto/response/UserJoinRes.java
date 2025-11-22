package com.example.project.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinRes {

    private boolean success;
    private Long userSeq;
    private String email;
    private String message;

    // 성공용 팩토리 메서드
    public static UserJoinRes success(Long userSeq, String email) {
        return new UserJoinRes(true, userSeq, email, "회원가입이 완료되었습니다.");
    }

    // 실패용 팩토리 메서드
    public static UserJoinRes fail(String email, String message) {
        return new UserJoinRes(false, null, email, message);
    }
}

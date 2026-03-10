package com.apt.mail.dto.request;

import lombok.Getter;
import lombok.Setter;

// 새 비밀번호 설정 요청 DTO
@Getter
@Setter
public class ResetPasswordReq {

    // URL로 전달받은 재설정 토큰
    private String token;

    // 새로 설정할 비밀번호
    private String newPassword;
}
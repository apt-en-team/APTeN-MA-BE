package com.apt.mail.dto.request;

import lombok.Getter;
import lombok.Setter;

// 비밀번호 재설정 이메일 요청 DTO
@Getter
@Setter
public class PasswordResetReq {

    // 비밀번호 재설정 링크를 받을 이메일
    private String email;
}
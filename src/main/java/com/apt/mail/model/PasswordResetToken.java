package com.apt.mail.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// 비밀번호 재설정 토큰 엔티티
@Getter
@Setter
public class PasswordResetToken {

    // 토큰 ID (PK)
    private long tokenId;

    // 사용자 ID (FK)
    private Long userId;

    // 재설정 토큰 값 (UUID)
    private String token;

    // 토큰 만료 시간 (30분)
    private LocalDateTime expiredAt;

    // 생성 시간
    private LocalDateTime createdAt;
}

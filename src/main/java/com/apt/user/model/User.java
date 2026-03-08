package com.apt.user.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// user 테이블과 1:1 매핑되는 모델 클래스
@Getter
@Setter
public class User {

    // 사용자 고유 ID (PK)
    private Long userId;

    // 소속 세대 ID (FK → household)
    private Long householdId;

    // 로그인 이메일
    private String email;

    // BCrypt 암호화된 비밀번호 (소셜 로그인은 빈 문자열)
    private String password;

    // 사용자 이름
    private String name;

    // 전화번호
    private String phone;

    // 권한 (RESIDENT / ADMIN)
    private String role;

    // 계정 상태 (PENDING / APPROVED / REJECTED)
    private String status;

    // 승인한 관리자 ID
    private Long approvedBy;

    // 승인 일시
    private LocalDateTime approvedAt;

    // 소셜 로그인 제공자 (google / kakao / naver)
    private String provider;

    // 소셜 로그인 제공자의 사용자 ID
    private String providerId;

    // 소프트 딜리트 여부
    private int isDeleted;

    // 탈퇴 일시
    private LocalDateTime deletedAt;

    // 가입 일시
    private LocalDateTime createdAt;

    // 수정 일시
    private LocalDateTime updatedAt;
}
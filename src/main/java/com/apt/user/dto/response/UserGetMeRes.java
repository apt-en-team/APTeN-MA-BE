package com.apt.user.dto.response;

import lombok.Getter;
import lombok.Setter;

// 내 정보 조회 응답 DTO (GET /api/users/me)
@Getter
@Setter
public class UserGetMeRes {

    // 사용자 고유 ID
    private Long userId;

    // 로그인 이메일
    private String email;

    // 사용자 이름
    private String name;

    // 전화번호
    private String phone;

    // 권한 (RESIDENT / ADMIN)
    private String role;

    // 계정 상태 (PENDING / APPROVED / REJECTED)
    private String status;

    // 소속 세대 ID
    private Long householdId;

    // 동
    private String dong;

    // 호
    private String ho;

    // 소셜 로그인 제공자 (google / kakao / naver / null)
    private String provider;
}
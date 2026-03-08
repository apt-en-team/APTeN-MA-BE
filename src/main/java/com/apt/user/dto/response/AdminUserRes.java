package com.apt.user.dto.response;

import lombok.Getter;
import lombok.Setter;

// 관리자 회원 조회 응답 DTO
// 승인/거부 처리 전 유저 정보 확인용
@Getter
@Setter
public class AdminUserRes {

    // 유저 고유 ID
    private Long userId;

    // 이름
    private String name;

    // 전화번호
    private String phone;

    // 소속 세대 ID (소셜 로그인이면 null 가능)
    private Long householdId;

    // 현재 상태 (PENDING / APPROVED / REJECTED)
    private String status;
}

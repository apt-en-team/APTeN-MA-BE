package com.apt.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// 소셜 로그인 후 동호수 연결 요청 DTO
// PATCH /api/auth/link-household 에서 사용
@Getter // 필드 getter 자동 생성 (dong, ho)
@Setter
public class LinkHouseholdReq {

    // @NotBlank: null, 빈 문자열, 공백 모두 거부
    // 유효성 검사 실패 시 GlobalExceptionHandler에서 400 반환
    @NotBlank(message = "동을 입력해주세요")
    private String dong;

    @NotBlank(message = "호수를 입력해주세요")
    private String ho;

    @NotBlank(message = "전화번호를 입력해주세요")
    private String phone;
}
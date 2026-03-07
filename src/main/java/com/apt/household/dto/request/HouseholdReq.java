package com.apt.household.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// API-015 세대 등록 / API-016 세대 수정 요청 DTO
// POST /api/admin/households
// PUT  /api/admin/households/:id
@Getter
@Setter
public class HouseholdReq {

    // 동 번호 (필수값, 예: 101동)
    @NotBlank(message = "동을 입력해주세요")
    private String dong;

    // 호수 (필수값, 예: 502호)
    @NotBlank(message = "호수를 입력해주세요")
    private String ho;
}

package com.apt.household.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

// API-018 세대 입주/퇴거 이력 등록 요청 DTO
// POST /api/admin/households/:id/history
@Getter
@Setter
public class HouseholdHistoryReq {

    // 입주 또는 퇴거하는 입주민 ID (필수값)
    @NotNull(message = "회원 ID를 입력해주세요")
    private Long userId;

    // 변경 상태 (필수값): '입주' 또는 '퇴거'
    // DB ENUM('입주', '퇴거') 와 일치해야 함
    @NotBlank(message = "상태를 선택해주세요")
    private String status;
}

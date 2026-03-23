package com.apt.visitorvehicle.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

// 관리자 방문차량 등록 요청 DTO
// POST /api/admin/visitor-vehicles 에서 @RequestBody로 바인딩
// 일반 등록과 달리 userId를 body로 직접 받음 (관리자가 입주민 대신 등록)
@Getter
@Setter
public class AdminVisitorVehicleReq {
    @NotNull
    private Long userId;          // 등록 대상 입주민 ID (필수)
    @NotBlank
    private String licensePlate;  // 차량번호 (필수)
    private String visitorName;   // 방문자 이름 (선택)
    private String visitPurpose;  // 방문 목적 (선택)
    @NotNull
    private LocalDate visitDate;  // 방문 예정일 (필수)
}
package com.apt.visitorvehicle.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// API-062 | 고정 방문차량 등록 요청 DTO
@Getter
@Setter
public class FixedVisitorVehicleReq {
    @NotBlank
    private String vehicleNumber;  // 차량번호 (필수)
    @NotBlank
    private String visitorName;    // 방문자 이름 (필수)
    private String purpose;        // 방문 목적 (선택)
    @NotNull
    private LocalDate startDate;   // 고정 시작일 (필수)
    private LocalDate endDate;     // 고정 종료일 (선택, NULL이면 무기한)
}


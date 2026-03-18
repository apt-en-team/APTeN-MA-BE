package com.apt.visitorvehicle.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/*
  방문차량 사전등록 요청 DTO (API-030)
  POST /api/visitor-vehicles 에서 @RequestBody로 바인딩
 */
@Getter
@Setter
public class VisitorVehicleReq {
    @NotBlank  // null, "", "   " 전부 차단 (String 전용 검증)
    private String licensePlate;  // 차량번호 (필수)
    private String visitorName;   // 방문자 이름 (선택)
    private String visitPurpose;  // 방문 목적 (선택)
    @NotNull   // null만 차단 (String이 아닌 타입에 사용)
    private LocalDate visitDate;  // 방문 예정일 (필수)
}
// com.apt.visitorvehicle.dto.request.VisitorVehicleReuseReq
package com.apt.visitorvehicle.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 방문차량 재사용 요청 DTO (API-033)
 * POST /api/visitor-vehicles/:id/reuse 에서 @RequestBody로 바인딩
 */
@Getter
@Setter
public class VisitorVehicleReuseReq {
    @NotNull  // 새 방문 예정일 필수
    private LocalDate visitDate;
}
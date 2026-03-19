package com.apt.vehicle.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/** 입출차 기록 응답 DTO | API-043 GET /api/vehicles/my-logs */
@Getter
@Builder
public class VehicleLogRes {

    /** 기록 ID */
    private Long logId;

    /** 차량 ID */
    private Long vehicleId;  // ← 추가

    /** 차량 번호판 */
    private String licensePlate;

    /** 입출차 구분 (IN: 입차 / OUT: 출차) */
    private String entryType;

    /** 기록 일시 */
    private LocalDateTime loggedAt;
}